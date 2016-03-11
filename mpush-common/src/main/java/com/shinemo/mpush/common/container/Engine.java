package com.shinemo.mpush.common.container;

import java.util.List;

public class Engine extends BaseLifeCycle{

	

	@Override
	public void start() {
		fireLifeCycleEvent(LifeCyclePhase.BEFORE_START, null);
		
		//TODO 真实的启动
		System.out.println("engine start");
		
		fireLifeCycleEvent(LifeCyclePhase.AFTER_START, null);
	}

	@Override
	public void stop() {
		fireLifeCycleEvent(LifeCyclePhase.BEFORE_STOP, null);
		
		//TODO 真实的停止
		System.out.println("engine stop");
		fireLifeCycleEvent(LifeCyclePhase.AFTER_STOP, null);
	}
	

}
