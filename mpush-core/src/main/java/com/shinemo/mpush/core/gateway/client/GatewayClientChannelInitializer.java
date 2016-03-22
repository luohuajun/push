package com.shinemo.mpush.core.gateway.client;

import com.shinemo.mpush.api.Client;
import com.shinemo.mpush.api.protocol.Command;
import com.shinemo.mpush.common.MessageDispatcher;
import com.shinemo.mpush.core.gateway.biz.handler.GatewayPushHandler;
import com.shinemo.mpush.netty.codec.PacketDecoder;
import com.shinemo.mpush.netty.codec.PacketEncoder;

import io.netty.channel.socket.SocketChannel;

public class GatewayClientChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel>{
	
	private GatewayClientChannelHandler channelHandler;
	
	public GatewayClientChannelInitializer(Client client) {
		MessageDispatcher receiver = new MessageDispatcher();
	    receiver.register(Command.GATEWAY_PUSH, new GatewayPushHandler());
	    channelHandler = new GatewayClientChannelHandler(client);
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
         ch.pipeline().addLast("decoder", new PacketDecoder());
         ch.pipeline().addLast("encoder", PacketEncoder.INSTANCE);
         ch.pipeline().addLast("handler", channelHandler);
	}
	
}
