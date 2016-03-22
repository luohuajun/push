package com.shinemo.mpush.client;

import java.util.concurrent.atomic.AtomicLong;

public class Request {
	
	private static final AtomicLong request_id = new AtomicLong(0);
    private String userId;
    private String content;
    private long timeout;
    private long id;

    private Request(String userId,String content,long timeout) {
    	this.id = newId();
    	this.userId = userId;
    	this.content = content;
    	this.timeout = timeout;
	}
    
    public static Request build(String userId,String content,long timeout){
    	Request request = new Request(userId,content,timeout);
    	return request;
    }
    
    public static Request build(String userId,String content){
    	return build(userId, content, 0);
    }
    
    public static AtomicLong getRequestId() {
		return request_id;
	}
	public String getUserId() {
		return userId;
	}

	public String getContent() {
		return content;
	}

	public long getTimeout() {
		return timeout;
	}

	public long getId() {
		return id;
	}

	private static long newId() {
        return request_id.getAndIncrement();
    }

}
