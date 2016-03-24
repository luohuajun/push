package com.shinemo.mpush.client.mock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.Future.Callback;

/**
 * 并发条件
 * 1、同时提交request
 * 2、同一个request的状态的修改
 * 3、定时任务扫描
 */
public class TestClient {
	
	private static final Logger log = LoggerFactory.getLogger(TestClient.class);
	
	private Executor pool = Executors.newFixedThreadPool(10);
	
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
		
		TestRequest request1 = TestRequest.build("doctor1", "hello world");
		TestRequest request2 = TestRequest.build("doctor2", "hello world2");
		CountDownLatch latch = new CountDownLatch(1);
		pool.execute(new Worker(request1, latch, client));
		pool.execute(new Worker(request2, latch, client));
	
	}
	
	
	public static class Worker implements Runnable{
		
		private TestRequest request;
		private CountDownLatch latch;
		private TestMpushClient client;
		public Worker(TestRequest request,CountDownLatch latch,TestMpushClient client) {
			this.request = request;
			this.latch = latch;
			this.client = client;
		}
		
		@Override
		public void run() {
			try {
				latch.await();
			} catch (InterruptedException e) {
			}
			client.newRequest(request).get();
		}
	}
}
