package com.shinemo.mpush.common.container;

import com.shinemo.mpush.common.container.Lifecycle.LifecyclePhase;

public class LifecycleEvent {

	//触发该事件的具体实例对象
	private Lifecycle lifecycle;
	
	//事件类型
	private Lifecycle.LifecyclePhase phase;
	
	//事件携带的数据
	private Object data;

	public LifecycleEvent(Lifecycle lifecycle, LifecyclePhase phase, Object data) {
		this.lifecycle = lifecycle;
		this.phase = phase;
		this.data = data;
	}

	public Lifecycle getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Lifecycle lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Lifecycle.LifecyclePhase getPhase() {
		return phase;
	}

	public void setPhase(Lifecycle.LifecyclePhase phase) {
		this.phase = phase;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
