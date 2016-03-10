package com.shinemo.mpush.common.redis.module;

import java.util.concurrent.atomic.AtomicBoolean;

import com.shinemo.mpush.common.container.BaseModule;
import com.shinemo.mpush.common.redis.RedisManage;
import com.shinemo.mpush.common.spi.ServiceContainer;

public class RedisModule extends BaseModule{

	private RedisManage redisManage = ServiceContainer.getInstance(RedisManage.class, "redisManage"); 
	
	private AtomicBoolean startFlag = new AtomicBoolean();
	private AtomicBoolean stopFlag = new AtomicBoolean();
	
	public RedisModule(Listener listener) {
		super(listener);
	}
	
	public RedisModule() {
		this(null);
	}
	
	@Override
	public void start(Listener listener) {
		if(startFlag.compareAndSet(false, true)){
			redisManage.init();
			if(listener!=null){
				listener.onSuccess(this);
			}
		}else{
			log.error("module has start:{}",this.getClass().getSimpleName());
		}
	}

	@Override
	public void stop(Listener listener) {
		if(stopFlag.compareAndSet(false, true)){
			redisManage.close();
		}else{
			log.error("module has stop:{}",this.getClass().getSimpleName());
		}
	}
}
