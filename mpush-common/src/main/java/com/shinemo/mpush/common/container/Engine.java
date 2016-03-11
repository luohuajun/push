package com.shinemo.mpush.common.container;

import java.util.List;

public class Engine implements LifeCycle{

	private BaseLifeCycle lifecycle = new BaseLifeCycle(this);
	
	@Override
	public void addLifeCycleListener(LifeCycleListener listener) {
		lifecycle.addLifeCycleListener(listener);
	}

	@Override
	public List<LifeCycleListener> getLifeCycleListeners() {
		return lifecycle.getListeners();
	}

	@Override
	public void start() {
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.BEFORE_START, null);
		
		//TODO 真实的启动
		System.out.println("engine start");
		
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.AFTER_START, null);
	}

	@Override
	public void stop() {
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.BEFORE_STOP, null);
		
		//TODO 真实的停止
		System.out.println("engine stop");
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.AFTER_STOP, null);
	}
	

}
