package com.shinemo.mpush.common.redis.plugin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.shinemo.mpush.api.RedisKey;
import com.shinemo.mpush.api.container.LifeCycle.LifeCyclePhase;
import com.shinemo.mpush.api.container.LifeCycleEvent;
import com.shinemo.mpush.api.container.LifeCycleListener;
import com.shinemo.mpush.common.redis.RedisManageUtil;
import com.shinemo.mpush.common.zk.listener.DataChangeListener;
import com.shinemo.mpush.common.zk.listener.impl.RedisPathListener;
import com.shinemo.mpush.tools.MPushUtil;

public class RedisPlugin implements LifeCycleListener{

	private static final Logger log = LoggerFactory.getLogger(RedisPlugin.class);
	
	protected List<DataChangeListener> dataChangeListeners = Lists.newArrayList();
	
	public RedisPlugin() {
		registerListener(new RedisPathListener());
	}
	
	@Override
	public void lifeCycleEvent(LifeCycleEvent event) {
		if(event.getPhase().equals(LifeCyclePhase.BEFORE_START)){ 
			initData();
		}else if(event.getPhase().equals(LifeCyclePhase.AFTER_START)){
			RedisManageUtil.del(RedisKey.getUserOnlineKey(MPushUtil.getExtranetIp()));
		}
	}

	public void initData(){
		for(DataChangeListener listener:dataChangeListeners){
			log.info("RedisPlugin initData:"+listener.getClass().getSimpleName());
			listener.initData();
		}
	}
	
	public void registerListener(DataChangeListener listener){
		dataChangeListeners.add(listener);
	}
}
