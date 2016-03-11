package com.shinemo.mpush.cs;

import com.shinemo.mpush.common.container.LifeCycleEvent;
import com.shinemo.mpush.common.container.LifeCycleListener;
import com.shinemo.mpush.common.container.LifeCycle.LifeCyclePhase;
import com.shinemo.mpush.common.redis.module.RedisModule;
import com.shinemo.mpush.common.zk.module.ZkModule;

public class ModuleManage {
	
	public static void main(String[] args) {
		
		ZkModule zkModule = new ZkModule();
		
		RedisModule redisModule = new RedisModule();
		
		zkModule.addLifeCycleListener(new LifeCycleListener() {
			
			@Override
			public void lifeCycleEvent(LifeCycleEvent event) {
				if(event.getPhase().equals(LifeCyclePhase.BEFORE_START)){
					
				}else if(event.getPhase().equals(LifeCyclePhase.AFTER_START)){
					
				}else if(event.getPhase().equals(LifeCyclePhase.BEFORE_STOP)){
					
				}else if(event.getPhase().equals(LifeCyclePhase.AFTER_STOP)){
					
				}
			}
		});
		
		redisModule.addLifeCycleListener(new LifeCycleListener() {
			
			@Override
			public void lifeCycleEvent(LifeCycleEvent event) {
				if(event.getPhase().equals(LifeCyclePhase.BEFORE_START)){
					
				}else if(event.getPhase().equals(LifeCyclePhase.AFTER_START)){
					
				}else if(event.getPhase().equals(LifeCyclePhase.BEFORE_STOP)){
					
				}else if(event.getPhase().equals(LifeCyclePhase.AFTER_STOP)){
					
				}
			}
		});
		
		
		zkModule.start();
		
		redisModule.start();
		
	}

}
