package com.shinemo.mpush.common.gateway.plugin;

import java.util.List;

import com.google.common.collect.Lists;
import com.shinemo.mpush.api.container.LifeCycleEvent;
import com.shinemo.mpush.api.container.LifeCycleListener;
import com.shinemo.mpush.api.container.LifeCycle.LifeCyclePhase;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.zk.ZkManage;
import com.shinemo.mpush.common.zk.listener.DataChangeListener;
import com.shinemo.mpush.common.zk.listener.impl.GatewayServerPathListener;

public class GatewayServerPlugin implements LifeCycleListener{

	protected List<DataChangeListener> dataChangeListeners = Lists.newArrayList();
	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage");
	
	public GatewayServerPlugin() {
		registerListener(new GatewayServerPathListener());
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
			zkManage.registerListener(listener);
		}
	}
	
	public void initData(){
		for(DataChangeListener listener:dataChangeListeners){
			listener.initData();
		}
	}
	
	public void registerListener(DataChangeListener listener){
		dataChangeListeners.add(listener);
	}
	
}
