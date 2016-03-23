package com.shinemo.mpush.client;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.Future.Callback;

public class TestClient {
	
	private static final Logger log = LoggerFactory.getLogger(TestClient.class);
	
	TestMpushClient client = new TestMpushClient();
	
	@Test
	public void test1(){
		
		client.request(TestRequest.build("doctor1", "hello world"));
		client.request(TestRequest.build("doctor1", "hello world2"));
		
	}
	
	@Test
	public void testCallBack(){
		client.request(TestRequest.build("doctor1", "hello world", new Callback() {
			
			@Override
			public void onTimeout(String userId) {
				log.error("onTimeout:"+userId);
			}
			
			@Override
			public void onSuccess(String userId) {
				log.error("onSuccess:"+userId);
			}
			
			@Override
			public void onOffline(String userId) {
				log.error("onOffline:"+userId);
			}
			
			@Override
			public void onFailure(String userId) {
				log.error("onFailure:"+userId);
			}
		}));
	}

	@Test
	public void testMulThread(){
		
	}
	
}
