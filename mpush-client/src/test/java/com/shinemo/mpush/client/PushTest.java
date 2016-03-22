package com.shinemo.mpush.client;

import org.junit.Test;

import com.shinemo.mpush.api.Future.Callback;

public class PushTest {
	
	MpushClient client = new MpushClient("", "", "");
	
	@Test
	public void test(){
		Request request = Request.build("doctor7", "hello world");
		client.newRequest(request).setCallback(new Callback() {
			
			@Override
			public void onTimeout(String userId) {
				
			}
			
			@Override
			public void onSuccess(String userId) {
				
			}
			
			@Override
			public void onOffline(String userId) {
				
			}
			
			@Override
			public void onFailure(String userId) {
				
			}
		}).get();
	}

}
