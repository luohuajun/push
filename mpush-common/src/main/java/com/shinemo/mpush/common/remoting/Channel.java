package com.shinemo.mpush.common.remoting;

import java.net.InetSocketAddress;

public interface Channel {
	
	InetSocketAddress getRemoteAddress();
	
	boolean isConnected();
	
	boolean hasAttribute(String key);
	
	Object getAttribute(String key);
	
	void setAttribute(String key,Object value);
	
	void removeAttribute(String key);
	
}
