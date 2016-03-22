package com.shinemo.mpush.client;

import java.util.concurrent.atomic.AtomicLong;

public class Request {
	
	private static final AtomicLong request_id = new AtomicLong(0);
    private String userId;
    private String content;
    private long timeout;
    private long id;

    public Request() {
    	this.id = newId();
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
