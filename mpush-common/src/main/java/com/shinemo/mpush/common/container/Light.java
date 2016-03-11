package com.shinemo.mpush.common.container;

import java.util.List;

public class Light implements Lifecycle{

	private BaseLifecycle lifecycle = new BaseLifecycle(this);
	
	private String name;
	
	public Light(String name) {
		this.name = name;
	}
	
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
		System.out.println("light " + this.getName() + ", start");
		lifecycle.fireLifecycleEvent(LifecyclePhase.AFTER_START, null);
	}

	@Override
	public void stop() {
		lifecycle.fireLifecycleEvent(LifecyclePhase.BEFORE_STOP, null);
		System.out.println("light " + this.getName() + ", stop");
		lifecycle.fireLifecycleEvent(LifecyclePhase.AFTER_STOP, null);
	}

	public BaseLifecycle getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(BaseLifecycle lifecycle) {
		this.lifecycle = lifecycle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
