package com.shinemo.mpush.common.zk.module;

import com.shinemo.mpush.api.container.BaseLifeCycle;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.zk.ZkConfig;
import com.shinemo.mpush.common.zk.ZkManage;

public class ZkModule extends  BaseLifeCycle{

	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage"); 
	
	private ZkConfig zkConfig = null;
	
	public ZkModule(String ipLists, String namespace,String digest) {
		zkConfig = new ZkConfig(ipLists,namespace,digest);
	}
	
	@Override
	public void start0() {
		zkManage.init(zkConfig);
	}

	@Override
	public void stop0() {
		zkManage.close();
	}

}
