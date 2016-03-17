package com.shinemo.mpush.core.admin;


import com.shinemo.mpush.core.admin.handler.AdminHandler;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class AdminChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel>{
	
    private AdminHandler adminHandler = new AdminHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		 ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		 ch.pipeline().addLast("decoder", new StringDecoder());
         ch.pipeline().addLast("encoder", new StringEncoder());
         ch.pipeline().addLast("handler", adminHandler);
	}

}
