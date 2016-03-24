package com.shinemo.mpush.client.mock;

import java.util.Map;
import java.util.concurrent.locks.LockSupport;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

public class TestMap {
	
	private Map<Integer,Integer> map = Maps.newConcurrentMap();
	
	@Before
	public void init(){
		for(int i = 0;i<100;i++){
			map.put(i, i);
		}
	}
	
	@Test
	public void test(){
		
		Runnable run1 = new Runnable() {
			
			@Override
			public void run() {
				for (Integer temp : map.values()) {
					if (map == null) {
						continue;
					}else{
						System.out.println("thread1:"+temp);
					}
				}
			}
		};
		
		Runnable run2 = new Runnable() {
			
			@Override
			public void run() {
				for(int i = 99;i>50;i--){
					map.remove(i);
					System.out.println("thread2:"+i);
				}
			}
		};
		
		Thread t1 = new Thread(run1);
		Thread t2 = new Thread(run2);
		t1.start();
		t2.start();
		
		LockSupport.park();
		
	}

	
	
	
}
