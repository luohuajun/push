package com.shinemo.mpush.client;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.shinemo.mpush.api.Future;
import com.shinemo.mpush.common.config.ConfigCenter;

public class MpushFuture implements Future<MpushFuture> {

	private static final Logger log = LoggerFactory.getLogger(MpushFuture.class);

	private static final Map<Long, MpushFuture> futures = Maps.newConcurrentMap();

	private final long id;
	
	private final int timeout;

	private final Lock lock = new ReentrantLock();

	private final Condition done = lock.newCondition();

	private final long start = System.currentTimeMillis();

	private volatile long sendTime;
	
	private Callback callback;
	
	public MpushFuture(Request request, int timeout) {
		this.id = request.getId();
		this.timeout = timeout;
		futures.put(id, this);
	}

	public Object get() {
		return get(timeout);
	}

	public Object get(int timeout) {
		if(timeout<=0){
			timeout = ConfigCenter.holder.gatewayRequestDefaultTimeout();
		}
		if(!isDone()){
			long start = System.currentTimeMillis();
			lock.lock();
			try{
				while(!isDone()){
					done.await(timeout, TimeUnit.MILLISECONDS);
					if(isDone()||System.currentTimeMillis()-start> timeout){
						break;
					}
				}
			}catch(InterruptedException e){
				throw new RuntimeException(e);
			}finally{
				lock.unlock();
			}
			
			if(!isDone()){
//				throw new TimeoutException(sent>0)
			}
		}
		return null;
	}

	public MpushFuture setCallback(Callback callback) {
		this.callback = callback;
		return this;
	}

	public boolean isDone() {
		return false;
	}
	
    private void doSend() {
    	sendTime = System.currentTimeMillis();
    }

}
