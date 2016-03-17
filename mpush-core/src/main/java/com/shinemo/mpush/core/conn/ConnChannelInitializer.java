package com.shinemo.mpush.core.conn;

import com.shinemo.mpush.api.connection.ConnectionManager;
import com.shinemo.mpush.api.protocol.Command;
import com.shinemo.mpush.common.MessageDispatcher;
import com.shinemo.mpush.core.conn.biz.handler.BindUserHandler;
import com.shinemo.mpush.core.conn.biz.handler.FastConnectHandler;
import com.shinemo.mpush.core.conn.biz.handler.HandshakeHandler;
import com.shinemo.mpush.core.conn.biz.handler.HeartBeatHandler;
import com.shinemo.mpush.core.conn.biz.handler.HttpProxyHandler;
import com.shinemo.mpush.core.conn.biz.handler.UnbindUserHandler;
import com.shinemo.mpush.netty.client.HttpClient;
import com.shinemo.mpush.netty.codec.PacketDecoder;
import com.shinemo.mpush.netty.codec.PacketEncoder;

import io.netty.channel.socket.SocketChannel;


public class ConnChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel>{
	
	private ConnChannelHandler channelHandler;
	
	public ConnChannelInitializer(ConnectionManager connectionManager,HttpClient httpClient) {
		
		MessageDispatcher receiver = new MessageDispatcher();
		receiver.register(Command.HEARTBEAT, new HeartBeatHandler());
		receiver.register(Command.HANDSHAKE, new HandshakeHandler());
		receiver.register(Command.BIND, new BindUserHandler());
		receiver.register(Command.UNBIND, new UnbindUserHandler());
		receiver.register(Command.FAST_CONNECT, new FastConnectHandler());
		receiver.register(Command.HTTP_PROXY, new HttpProxyHandler(httpClient));
		channelHandler = new ConnChannelHandler(connectionManager, receiver);
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("decoder", new PacketDecoder());
        ch.pipeline().addLast("encoder", PacketEncoder.INSTANCE);
        ch.pipeline().addLast("handler", channelHandler);
	}
	
}
