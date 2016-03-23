package com.shinemo.mpush.client;

import java.util.concurrent.atomic.AtomicLong;

import com.shinemo.mpush.api.Future.Callback;

public class TestRequest {
	
	private static final AtomicLong request_id = new AtomicLong(0);
    private String userId;
    private String content;
    private long timeout;
    private long id;
    private Callback callback;

    private TestRequest(String userId,String content,long timeout,Callback callback) {
    	this.id = newId();
    	this.userId = userId;
    	this.content = content;
    	this.timeout = timeout;
    	this.callback = callback;
	}
    
    public static TestRequest build(String userId,String content,long timeout,Callback callback){
    	TestRequest request = new TestRequest(userId,content,timeout,callback);
    	return request;
    }
    
    public static TestRequest build(String userId,String content,Callback callback){
    	return build(userId, content, 0,callback);
    }
    
    public static TestRequest build(String userId,String content){
    	return build(userId, content, 0,null);
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

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
}
