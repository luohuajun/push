package com.shinemo.mpush.common.container;

import java.util.List;

public class Engine implements Lifecycle{

	private BaseLifecycle lifecycle = new BaseLifecycle(this);
	
	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		lifecycle.addLifecycleListener(listener);
	}

	@Override
	public List<LifecycleListener> getLifecycleListeners() {
		return lifecycle.getListeners();
	}

	@Override
	public void start() {
		lifecycle.fireLifecycleEvent(LifecyclePhase.BEFORE_START, null);
		
		//TODO 真实的启动
		System.out.println("engine start");
		
		lifecycle.fireLifecycleEvent(LifecyclePhase.AFTER_START, null);
	}

	@Override
	public void stop() {
		lifecycle.fireLifecycleEvent(LifecyclePhase.BEFORE_STOP, null);
		
		//TODO 真实的停止
		System.out.println("engine stop");
		lifecycle.fireLifecycleEvent(LifecyclePhase.AFTER_STOP, null);
	}
	

}
