package com.shinemo.mpush.common.container;

import java.util.List;

public class Light implements LifeCycle{

	private BaseLifeCycle lifecycle = new BaseLifeCycle(this);
	
	private String name;
	
	public Light(String name) {
		this.name = name;
	}
	
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
		System.out.println("light " + this.getName() + ", start");
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.AFTER_START, null);
	}

	@Override
	public void stop() {
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.BEFORE_STOP, null);
		System.out.println("light " + this.getName() + ", stop");
		lifecycle.fireLifeCycleEvent(LifeCyclePhase.AFTER_STOP, null);
	}

	public BaseLifeCycle getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(BaseLifeCycle lifecycle) {
		this.lifecycle = lifecycle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
