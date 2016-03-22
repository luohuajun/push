package com.shinemo.mpush.api;

import io.netty.channel.ChannelHandler;

public interface ChannelClientHandler extends ChannelHandler{
	
	public Client getClient();

}
