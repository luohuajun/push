package com.shinemo.mpush.api.container;

import java.util.List;

public interface LifeCycle {

	void addLifeCycleListener(LifeCycleListener listener);
	
	List<LifeCycleListener> getLifeCycleListeners();
	
	void start();
	
	void stop();
	
	public static enum LifeCyclePhase{
		BEFORE_START,AFTER_START,BEFORE_STOP,AFTER_STOP
	}
}
