package com.shinemo.mpush.common.zk.module;

import com.shinemo.mpush.common.container.BaseLifeCycle;
import com.shinemo.mpush.common.spi.ServiceContainer;
import com.shinemo.mpush.common.zk.ZkManage;

public class ZkModule extends  BaseLifeCycle{

	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage"); 
	
	@Override
	public void start0() {
		zkManage.init();
	}

	@Override
	public void stop0() {
		zkManage.close();
	}

}
