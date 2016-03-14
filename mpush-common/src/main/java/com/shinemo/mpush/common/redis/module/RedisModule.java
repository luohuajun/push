package com.shinemo.mpush.common.redis.module;

import com.shinemo.mpush.api.container.BaseLifeCycle;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.redis.RedisManage;
import com.shinemo.mpush.common.redis.plugin.RedisPlugin;

public class RedisModule extends BaseLifeCycle{

	private RedisManage redisManage = ServiceContainer.getInstance(RedisManage.class, "redisManage"); 
	
	public RedisModule() {
		addLifeCycleListener(new RedisPlugin());
	}
	
	@Override
	public void start0() {
		redisManage.init();
	}
	
}
