package com.shinemo.mpush.common.container;


public class Light extends BaseLifeCycle{

	private String name;
	
	public Light(String name) {
		this.name = name;
	}
	

	@Override
	public void start() {
		fireLifeCycleEvent(LifeCyclePhase.BEFORE_START, null);
		System.out.println("light " + this.getName() + ", start");
		fireLifeCycleEvent(LifeCyclePhase.AFTER_START, null);
	}

	@Override
	public void stop() {
		fireLifeCycleEvent(LifeCyclePhase.BEFORE_STOP, null);
		System.out.println("light " + this.getName() + ", stop");
		fireLifeCycleEvent(LifeCyclePhase.AFTER_STOP, null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
