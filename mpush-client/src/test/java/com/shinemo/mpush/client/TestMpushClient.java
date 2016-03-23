package com.shinemo.mpush.client;


import com.shinemo.mpush.api.Future.Callback;
import com.shinemo.mpush.api.exception.PushMessageException;

public class TestMpushClient {

	public TestMpushFuture newRequest(TestRequest request) {
		TestMpushFuture future = new TestMpushFuture(request, 0);
		try {
			send(request);
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

	private void send(final TestRequest request) {
		
		String userId = request.getUserId();
		final Callback callback = request.getCallback();
		
		if(userId.equals("doctor1")){
			TestMpushFuture.done(request.getId());
			if (callback != null) {
				callback.onOffline(request.getUserId());
			}
			return;
		}
		
		if(userId.equals("doctor2")){
			TestMpushFuture.done(request.getId());
			if (callback != null) {
				callback.onFailure(request.getUserId());
			}
			return;
		}
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
		TestMpushFuture.done(request.getId());
		if (callback != null) {
			callback.onSuccess(request.getUserId());
		}
	}

}
