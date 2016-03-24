package com.shinemo.mpush.client;

import org.junit.Test;

public class PushTest {
	
	MpushClient client = new MpushClient("", "", "");
	
	@Test
	public void test(){
		Request request = Request.build("doctor7", "hello world");
		client.newRequest(request).get();
	}

}
