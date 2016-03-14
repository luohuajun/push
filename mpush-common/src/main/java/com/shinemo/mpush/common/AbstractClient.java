package com.shinemo.mpush.common;

import java.util.List;

import com.google.common.collect.Lists;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.common.redis.RedisGroup;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.common.zk.ZkManage;
import com.shinemo.mpush.common.zk.listener.DataChangeListener;
import com.shinemo.mpush.common.zk.listener.impl.RedisPathListener;
import com.shinemo.mpush.tools.Jsons;

public abstract class AbstractClient {
	
    protected List<DataChangeListener> dataChangeListeners = Lists.newArrayList();
    
    protected ZkManage zkRegister = ServiceContainer.getInstance(ZkManage.class);
    
	public AbstractClient() {
		registerListener(new RedisPathListener());
	}
	
	
	public void registerListener(DataChangeListener listener){
		dataChangeListeners.add(listener);
	}

	//step1 启动 zk
	private void initZK(){
    	zkRegister.init();
	}
	
	//step2 获取redis
	private void initRedis(){
		boolean exist = zkRegister.isExisted(ZKPath.REDIS_SERVER.getPath());
        if (!exist) {
            List<RedisGroup> groupList = ConfigCenter.holder.redisGroups();
            zkRegister.registerPersist(ZKPath.REDIS_SERVER.getPath(), Jsons.toJson(groupList));
        }
	}
	
	//step3 注册listener
	private void registerListeners(){
		for(DataChangeListener listener:dataChangeListeners){
			zkRegister.registerListener(listener);
		}
	}
	
	//step4 初始化 listener data
	private void initListenerData(){
		for(DataChangeListener listener:dataChangeListeners){
			listener.initData();
		}
	}
	
	public void start(){
		initZK();
		initRedis();
		registerListeners();
		initListenerData();
	}
	
}
