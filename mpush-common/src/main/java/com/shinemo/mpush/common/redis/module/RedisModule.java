package com.shinemo.mpush.common.redis.module;

import com.shinemo.mpush.common.container.BaseLifeCycle;
import com.shinemo.mpush.common.redis.RedisManage;
import com.shinemo.mpush.common.spi.ServiceContainer;

public class RedisModule extends BaseLifeCycle{

	private RedisManage redisManage = ServiceContainer.getInstance(RedisManage.class, "redisManage"); 
	
	@Override
	public void start0() {
		redisManage.init();
	}
	
}
