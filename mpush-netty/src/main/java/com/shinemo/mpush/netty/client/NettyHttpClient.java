package com.shinemo.mpush.netty.client;

import com.google.common.collect.ArrayListMultimap;
import com.shinemo.mpush.tools.config.ConfigCenter;
import com.shinemo.mpush.tools.thread.NamedThreadFactory;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ohun on 2016/2/15.
 */
public class NettyHttpClient implements HttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpClient.class);

    private final int maxConnPerHost = ConfigCenter.holder.maxHttpConnCountPerHost();
    private final AttributeKey<RequestInfo> requestKey = AttributeKey.newInstance("request");
    private final AttributeKey<String> hostKey = AttributeKey.newInstance("host");
    private final ArrayListMultimap<String, Channel> channelPool = ArrayListMultimap.create();

    private Bootstrap b;
    private EventLoopGroup workerGroup;
    private Timer timer;

    @Override
    public void start() {
        workerGroup = new NioEventLoopGroup(0, ThreadPoolManager.httpExecutor);
        b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.option(ChannelOption.SO_REUSEADDR, true);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 4000);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", new HttpResponseDecoder());
                ch.pipeline().addLast("aggregator", new HttpObjectAggregator(1024 * 1024 * 5));//5M
                ch.pipeline().addLast("encoder", new HttpRequestEncoder());
                ch.pipeline().addLast("handler", new HttpClientHandler());
            }
        });
        timer = new HashedWheelTimer(new NamedThreadFactory("http-client-timer-"),
                1, TimeUnit.SECONDS, 64);
    }

    @Override
    public void stop() {
        for (Channel channel : channelPool.values()) {
            channel.close();
        }
        channelPool.clear();
        workerGroup.shutdownGracefully();
        timer.stop();
    }

    @Override
    public void request(final RequestInfo info) throws Exception {
        URI uri = new URI(info.request.uri());
        String host = info.host = uri.getHost();
        int port = uri.getPort() == -1 ? 80 : uri.getPort();
        info.request.headers().set(HttpHeaderNames.HOST, host);
        info.request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        timer.newTimeout(info, info.readTimeout, TimeUnit.MILLISECONDS);
        Channel channel = tryAcquire(host);
        if (channel == null) {
            final long startConnectChannel = System.currentTimeMillis();
            LOGGER.debug("create new channel, host={}", host);
            ChannelFuture f = b.connect(host, port);
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    LOGGER.debug("create new channel cost:" + (System.currentTimeMillis() - startConnectChannel));
                    if (future.isSuccess()) {
                        writeRequest(future.channel(), info);
                    } else {
                        info.tryDone();
                        info.onFailure(504, "Gateway Timeout");
                        LOGGER.debug("request failure request={}", info);
                    }
                }
            });
        } else {
            writeRequest(channel, info);
        }
    }

    private synchronized Channel tryAcquire(String host) {
        List<Channel> channels = channelPool.get(host);
        if (channels == null || channels.isEmpty()) return null;
        Iterator<Channel> it = channels.iterator();
        while (it.hasNext()) {
            Channel channel = it.next();
            it.remove();
            if (channel.isActive()) {
                LOGGER.debug("tryAcquire channel success, host={}", host);
                channel.attr(hostKey).set(host);
                return channel;
            } else {//链接由于意外情况不可用了，keepAlive_timeout
                LOGGER.error("tryAcquire channel false channel is inactive, host={}", host);
            }
        }
        return null;
    }

    private synchronized void tryRelease(Channel channel) {
        String host = channel.attr(hostKey).getAndRemove();
        List<Channel> channels = channelPool.get(host);
        if (channels == null || channels.size() < maxConnPerHost) {
            LOGGER.debug("tryRelease channel success, host={}", host);
            channelPool.put(host, channel);
        } else {
            LOGGER.debug("tryRelease channel pool size over limit={}, host={}, channel closed.", maxConnPerHost, host);
            channel.close();
        }
    }

    private void writeRequest(Channel channel, RequestInfo info) {
        channel.attr(requestKey).set(info);
        channel.attr(hostKey).set(info.host);
        channel.writeAndFlush(info.request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    RequestInfo info = future.channel().attr(requestKey).getAndRemove();
                    info.tryDone();
                    info.onFailure(503, "Service Unavailable");
                    LOGGER.debug("request failure request={}", info);
                    tryRelease(future.channel());
                }
            }
        });
    }

    @ChannelHandler.Sharable
    private class HttpClientHandler extends ChannelHandlerAdapter {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            RequestInfo info = ctx.channel().attr(requestKey).getAndRemove();
            try {
                if (info != null && info.tryDone()) {
                    info.onException(cause);
                }
            } finally {
                tryRelease(ctx.channel());
            }
            LOGGER.error("http client caught an error, info={}", info, cause);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            RequestInfo info = ctx.channel().attr(requestKey).getAndRemove();
            try {
                if (info != null && info.tryDone()) {
                    HttpResponse response = (HttpResponse) msg;
                    if (isRedirect(response)) {
                        if (info.onRedirect(response)) {
                            String location = getRedirectLocation(info.request, response);
                            if (location != null && location.length() > 0) {
                                info.cancelled.set(false);
                                info.request = info.request.copy().setUri(location);
                                request(info);
                                return;
                            }
                        }
                    }
                    info.onResponse(response);
                    LOGGER.debug("request done request={}", info);
                }
            } finally {
                tryRelease(ctx.channel());
                ReferenceCountUtil.release(msg);
            }
        }

        private boolean isRedirect(HttpResponse response) {
            HttpResponseStatus status = response.status();
            switch (status.code()) {
                case 300:
                case 301:
                case 302:
                case 303:
                case 305:
                case 307:
                    return true;
                default:
                    return false;
            }
        }

        private String getRedirectLocation(HttpRequest request, HttpResponse response) throws Exception {
            String hdr = URLDecoder.decode(response.headers().get(HttpHeaderNames.LOCATION).toString(), "UTF-8");
            if (hdr != null) {
                if (hdr.toLowerCase().startsWith("http://") || hdr.toLowerCase().startsWith("https://")) {
                    return hdr;
                } else {
                    URL orig = new URL(request.uri());
                    String pth = orig.getPath() == null ? "/" : URLDecoder.decode(orig.getPath().toString(), "UTF-8");
                    if (hdr.startsWith("/")) {
                        pth = hdr;
                    } else if (pth.endsWith("/")) {
                        pth += hdr;
                    } else {
                        pth += "/" + hdr;
                    }
                    StringBuilder sb = new StringBuilder(orig.getProtocol().toString());
                    sb.append("://").append(orig.getHost());
                    if (orig.getPort() > 0) {
                        sb.append(":").append(orig.getPort());
                    }
                    if (pth.charAt(0) != '/') {
                        sb.append('/');
                    }
                    sb.append(pth);
                    return sb.toString();
                }
            }
            return null;
        }

        private HttpRequest copy(String uri, HttpRequest request) {
            HttpRequest nue = request;
            if (request instanceof DefaultFullHttpRequest) {
                DefaultFullHttpRequest dfrq = (DefaultFullHttpRequest) request;
                FullHttpRequest rq;
                try {
                    rq = dfrq.copy();
                } catch (IllegalReferenceCountException e) { // Empty bytebuf
                    rq = dfrq;
                }
                rq.setUri(uri);
            } else {
                DefaultHttpRequest dfr = new DefaultHttpRequest(request.protocolVersion(), request.method(), uri);
                dfr.headers().set(request.headers());
                nue = dfr;
            }
            return nue;
        }
    }
}
