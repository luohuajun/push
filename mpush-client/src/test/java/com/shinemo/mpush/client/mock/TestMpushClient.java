package com.shinemo.mpush.client.mock;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.shinemo.mpush.api.Future.Callback;
import com.shinemo.mpush.api.exception.PushMessageException;

public class TestMpushClient {
	
	private Executor pool = Executors.newFixedThreadPool(5);

	public TestMpushFuture newRequest(TestRequest request) {
		TestMpushFuture future = new TestMpushFuture(request, 0);
		try {
			pool.execute(new Worker(request));
		} catch (PushMessageException e) {
			future.cancel();
			throw e;
		}
		return future;
	}
	
	//nothing to return
	public void request(TestRequest request){
		TestMpushFuture future = newRequest(request);
		future.get();
	}
	
	public static class Worker implements Runnable{
		
		private final TestRequest request;
		
		public Worker(final TestRequest request) {
			this.request = request;
		}
		
		@Override
		public void run() {
			
			String userId = request.getUserId();
			final Callback callback = request.getCallback();
			
			if(userId.equals("doctor1")){
				TestMpushFuture.finishRequest(request.getId());
				if (callback != null) {
					callback.onOffline(request.getUserId());
				}
				return;
			}
			
			if(userId.equals("doctor2")){
				TestMpushFuture.finishRequest(request.getId());
				if (callback != null) {
					callback.onFailure(request.getUserId());
				}
				return;
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			
			TestMpushFuture.finishRequest(request.getId());
			if (callback != null) {
				callback.onSuccess(request.getUserId());
			}
			
		}
	}
	

}
