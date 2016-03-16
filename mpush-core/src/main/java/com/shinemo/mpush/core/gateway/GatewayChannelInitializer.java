package com.shinemo.mpush.core.gateway;

import com.shinemo.mpush.api.connection.ConnectionManager;
import com.shinemo.mpush.api.protocol.Command;
import com.shinemo.mpush.common.MessageDispatcher;
import com.shinemo.mpush.core.handler.GatewayPushHandler;
import com.shinemo.mpush.core.server.ServerChannelHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class GatewayChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel>{
	
	private ServerChannelHandler channelHandler;
	
	public GatewayChannelInitializer(ConnectionManager connectionManager) {
		MessageDispatcher receiver = new MessageDispatcher();
	    receiver.register(Command.GATEWAY_PUSH, new GatewayPushHandler());
	    channelHandler = new ServerChannelHandler(false, connectionManager, receiver);
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		 ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		 ch.pipeline().addLast("decoder", new StringDecoder());
         ch.pipeline().addLast("encoder", new StringEncoder());
         ch.pipeline().addLast("handler", channelHandler);
	}
	
}
