package com.shinemo.mpush.common.redis.listener;


public interface MessageListener {
	
	void onMessage(String channel, String message);

}
