package com.shinemo.mpush.test.push;

import com.shinemo.mpush.api.Future;
import com.shinemo.mpush.client.MpushClient;
import com.shinemo.mpush.client.Request;
import java.util.concurrent.locks.LockSupport;

public class Main {
    public static void main(String[] args) throws Exception {
    	
    	MpushClient client = new MpushClient("127.0.0.1:2181", "mpush-daily", "shinemoIpo");
    	
    	client.init();
    	
        for (int i = 0; i < 10; i++) {
        	
        	client.newRequest(Request.build("huang1-"+i, "hello world", new Future.Callback() {
				
				@Override
				public void onTimeout(String userId) {
					 System.err.println("push onTimeout userId=" + userId);
				}
				
				@Override
				public void onSuccess(String userId) {
					System.err.println("push onSuccess userId=" + userId);
				}
				
				@Override
				public void onOffline(String userId) {
					System.err.println("push onOffline userId=" + userId);
				}
				
				@Override
				public void onFailure(String userId) {
					 System.err.println("push onFailure userId=" + userId);
				}
			}));
        	
        }
        LockSupport.park();
    }

}