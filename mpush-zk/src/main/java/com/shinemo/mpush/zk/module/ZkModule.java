package com.shinemo.mpush.zk.module;

import java.util.concurrent.atomic.AtomicBoolean;

import com.shinemo.mpush.common.container.BaseModule;
import com.shinemo.mpush.tools.spi.ServiceContainer;
import com.shinemo.mpush.zk.ZkManage;

public class ZkModule extends  BaseModule{

	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage"); 
	
	private AtomicBoolean startFlag = new AtomicBoolean();
	private AtomicBoolean stopFlag = new AtomicBoolean();
	
	public ZkModule(Listener listener) {
		super(listener);
	}
	
	public ZkModule() {
		this(null);
	}

	@Override
	public void start(Listener listener) {
		if(startFlag.compareAndSet(false, true)){
			zkManage.init();
			if(listener!=null){
				listener.onSuccess(this);
			}
		}else{
			log.error("module has start:{}",this.getClass().getSimpleName());
		}
	}

	@Override
	public void stop(Listener listener) {
		if(stopFlag.compareAndSet(false, true)){
			zkManage.close();
		}else{
			log.error("module has stop:{}",this.getClass().getSimpleName());
		}
		
	}

}
