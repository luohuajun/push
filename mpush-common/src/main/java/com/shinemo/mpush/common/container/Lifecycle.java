package com.shinemo.mpush.common.container;

import java.util.List;

public interface Lifecycle {

	void addLifecycleListener(LifecycleListener listener);
	
	List<LifecycleListener> getLifecycleListeners();
	
	void start();
	
	void stop();
	
	public static enum LifecyclePhase{
		BEFORE_START,AFTER_START,BEFORE_STOP,AFTER_STOP
	}
}
