package com.shinemo.mpush.common.redis.plugin;

import com.shinemo.mpush.api.RedisKey;
import com.shinemo.mpush.common.container.LifeCycle.LifeCyclePhase;
import com.shinemo.mpush.common.container.LifeCycleEvent;
import com.shinemo.mpush.common.container.LifeCycleListener;
import com.shinemo.mpush.common.redis.RedisManageUtil;
import com.shinemo.mpush.tools.MPushUtil;

public class RedisPlugin implements LifeCycleListener{

	@Override
	public void lifeCycleEvent(LifeCycleEvent event) {
		if(event.getPhase().equals(LifeCyclePhase.AFTER_START)){
			RedisManageUtil.del(RedisKey.getUserOnlineKey(MPushUtil.getExtranetIp()));
		}
	}

}
