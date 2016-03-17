package com.shinemo.mpush.common.conn.plugin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.shinemo.mpush.api.container.LifeCycleEvent;
import com.shinemo.mpush.api.container.LifeCycleListener;
import com.shinemo.mpush.api.container.LifeCycle.LifeCyclePhase;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.zk.ZkManage;
import com.shinemo.mpush.common.zk.listener.DataChangeListener;
import com.shinemo.mpush.common.zk.listener.impl.ConnectionServerPathListener;

public class ConnServerPlugin implements LifeCycleListener{

	protected List<DataChangeListener> dataChangeListeners = Lists.newArrayList();
	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage");
	private static final Logger log = LoggerFactory.getLogger(ConnServerPlugin.class);
	
	public ConnServerPlugin() {
		registerListener(new ConnectionServerPathListener());
	}
	
	@Override
	public void lifeCycleEvent(LifeCycleEvent event) {
		if(event.getPhase().equals(LifeCyclePhase.BEFORE_START)){
			registerToZk();
			initData();
		}
	}
	
	public void registerToZk(){
		for(DataChangeListener listener:dataChangeListeners){
			log.info("ConnServerPlugin registerToZk:"+listener.getClass().getSimpleName());
			zkManage.registerListener(listener);
		}
	}
	
	public void initData(){
		for(DataChangeListener listener:dataChangeListeners){
			log.info("ConnServerPlugin initData:"+listener.getClass().getSimpleName());
			listener.initData();
		}
	}
	
	public void registerListener(DataChangeListener listener){
		dataChangeListeners.add(listener);
	}
	
}
