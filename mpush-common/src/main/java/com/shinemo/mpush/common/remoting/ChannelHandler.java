package com.shinemo.mpush.common.remoting;

public interface ChannelHandler<T> {
	
	void connected(Channel channel) throws RemotingException;
	
	void disconnected(Channel channel) throws RemotingException;
	
	void sent(Channel channel, Object message) throws RemotingException;

	void received(Channel channel, Object message) throws RemotingException;
	
	void caught(Channel channel, Throwable exception) throws RemotingException;
}
