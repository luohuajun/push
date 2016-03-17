package com.shinemo.mpush.api.container;

import com.shinemo.mpush.api.container.LifeCycle.LifeCyclePhase;


public class LifeCycleEvent {

	//触发该事件的具体实例对象
	private transient LifeCycle lifecycle;
	
	//事件类型
	private LifeCycle.LifeCyclePhase phase;
	
	//事件携带的数据
	private Object data;

	public LifeCycleEvent(LifeCycle lifecycle, LifeCyclePhase phase, Object data) {
		this.lifecycle = lifecycle;
		this.phase = phase;
		this.data = data;
	}

	public LifeCycle getLifeCycle() {
		return lifecycle;
	}

	public void setLifecycle(LifeCycle lifecycle) {
		this.lifecycle = lifecycle;
	}

	public LifeCycle.LifeCyclePhase getPhase() {
		return phase;
	}

	public void setPhase(LifeCycle.LifeCyclePhase phase) {
		this.phase = phase;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return lifecycle.getClass().getSimpleName()+",LifeCycleEvent [phase=" + phase + ", data=" + data + "]";
	}
	
}
