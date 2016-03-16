package com.shinemo.mpush.core.conn;

import com.shinemo.mpush.api.connection.ConnectionManager;
import com.shinemo.mpush.api.protocol.Command;
import com.shinemo.mpush.common.MessageDispatcher;
import com.shinemo.mpush.core.handler.BindUserHandler;
import com.shinemo.mpush.core.handler.FastConnectHandler;
import com.shinemo.mpush.core.handler.HandshakeHandler;
import com.shinemo.mpush.core.handler.HeartBeatHandler;
import com.shinemo.mpush.core.handler.HttpProxyHandler;
import com.shinemo.mpush.core.handler.UnbindUserHandler;
import com.shinemo.mpush.core.server.ServerChannelHandler;
import com.shinemo.mpush.netty.client.HttpClient;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class ConnChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel>{
	
	private ServerChannelHandler channelHandler;
	
	public ConnChannelInitializer(ConnectionManager connectionManager,HttpClient httpClient) {
		
		MessageDispatcher receiver = new MessageDispatcher();
		receiver.register(Command.HEARTBEAT, new HeartBeatHandler());
		receiver.register(Command.HANDSHAKE, new HandshakeHandler());
		receiver.register(Command.BIND, new BindUserHandler());
		receiver.register(Command.UNBIND, new UnbindUserHandler());
		receiver.register(Command.FAST_CONNECT, new FastConnectHandler());
		receiver.register(Command.HTTP_PROXY, new HttpProxyHandler(httpClient));
		channelHandler = new ServerChannelHandler(true, connectionManager, receiver);
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		 ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		 ch.pipeline().addLast("decoder", new StringDecoder());
         ch.pipeline().addLast("encoder", new StringEncoder());
         ch.pipeline().addLast("handler", channelHandler);
	}
	
}
