package com.shinemo.mpush.zk.module;

import com.shinemo.mpush.common.container.BaseModule;
import com.shinemo.mpush.tools.spi.ServiceContainer;
import com.shinemo.mpush.zk.ZkManage;

public class ZkModule extends  BaseModule{

	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage"); 
	
	public ZkModule(Listener listener) {
		super(listener);
	}
	
	public ZkModule() {
		this(null);
	}

	@Override
	public void start(Listener listener) {
		zkManage.init();
		if(listener!=null){
			listener.onSuccess(this);
		}
	}

	@Override
	public void stop(Listener listener) {
		zkManage.close();
	}

}
