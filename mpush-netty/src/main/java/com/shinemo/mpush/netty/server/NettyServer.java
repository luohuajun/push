package com.shinemo.mpush.netty.server;

import com.shinemo.mpush.api.Server;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class NettyServer implements Server {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    private AtomicBoolean startFlag = new AtomicBoolean();
    private AtomicBoolean stopFlag = new AtomicBoolean();

    private int port;
    private Executor bossExecutor;
    private Executor workExecutor;
    private ChannelInitializer<?> channelInitializer;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    
    @Override
    public void init(int port,Executor bossExecutor,Executor workExecutor,ChannelInitializer<?> channelInitializer) {
    	this.port = port;
    	this.bossExecutor = bossExecutor;
    	this.workExecutor = workExecutor;
    	this.channelInitializer = channelInitializer;
	}

    @Override
    public boolean isRunning() {
        return startFlag.get();
    }

    @Override
    public void stop(Listener listener) {
        if (!stopFlag.compareAndSet(false,true)) {
            throw new IllegalStateException("The server is already shutdown.");
        }
        if (workerGroup != null) workerGroup.shutdownGracefully().syncUninterruptibly();
        if (bossGroup != null) bossGroup.shutdownGracefully().syncUninterruptibly();
        log.error("netty server stop now:"+port);
    }

    @Override
    public void start(final Listener listener) {
        if (!startFlag.compareAndSet(false, true)) {
            throw new IllegalStateException("Server already started or have not init");
        }
        createNioServer(listener);
    }

    private void createServer(final Listener listener, EventLoopGroup boss, EventLoopGroup work, Class<? extends ServerChannel> clazz) {
        this.bossGroup = boss;
        this.workerGroup = work;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(clazz);
            b.childHandler(channelInitializer);
//            b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
//                @Override
//                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast("decoder", new PacketDecoder());
//                    ch.pipeline().addLast("encoder", PacketEncoder.INSTANCE);
//                    ch.pipeline().addLast("handler", getChannelHandler());
//                }
//            });

            initOptions(b);

            /***
             * 绑定端口并启动去接收进来的连接
             */
            ChannelFuture f = b.bind(port).sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                    	log.error("server start success on:" + port);
                        if (listener != null) listener.onSuccess();
                    } else {
                        log.error("server start failure on:" + port);
                        if (listener != null) listener.onFailure("start server failure");
                    }
                }
            });
            if (f.isSuccess()) {
                f.channel().closeFuture().sync();
            }

        } catch (Exception e) {
            log.error("server start exception", e);
            if (listener != null) listener.onFailure("start server ex=" + e.getMessage());
            throw new RuntimeException("server start exception, port=" + port, e);
        } finally {
            /***
             * 优雅关闭
             */
            stop(null);
        }
    }

    private void createNioServer(final Listener listener) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1, bossExecutor);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(0, workExecutor);
        createServer(listener, bossGroup, workerGroup, NioServerSocketChannel.class);
    }


    @SuppressWarnings("unused")
    private void createEpollServer(final Listener listener) {
        EpollEventLoopGroup bossGroup = new EpollEventLoopGroup(1, ThreadPoolManager.bossExecutor);
        EpollEventLoopGroup workerGroup = new EpollEventLoopGroup(0, ThreadPoolManager.workExecutor);
        createServer(listener, bossGroup, workerGroup, EpollServerSocketChannel.class);
    }

    protected void initOptions(ServerBootstrap b) {

        /***
         * option()是提供给NioServerSocketChannel用来接收进来的连接。
         * childOption()是提供给由父管道ServerChannel接收到的连接，
         * 在这个例子中也是NioServerSocketChannel。
         */
        b.childOption(ChannelOption.SO_KEEPALIVE, true);


        /**
         * 在Netty 4中实现了一个新的ByteBuf内存池，它是一个纯Java版本的 jemalloc （Facebook也在用）。
         * 现在，Netty不会再因为用零填充缓冲区而浪费内存带宽了。不过，由于它不依赖于GC，开发人员需要小心内存泄漏。
         * 如果忘记在处理程序中释放缓冲区，那么内存使用率会无限地增长。
         * Netty默认不使用内存池，需要在创建客户端或者服务端的时候进行指定
         */
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }
}
