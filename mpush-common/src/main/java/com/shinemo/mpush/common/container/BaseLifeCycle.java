package com.shinemo.mpush.common.container;

import java.util.List;

import com.google.common.collect.Lists;

public class BaseLifeCycle{
	
	private LifeCycle lifecycle;
	private List<LifeCycleListener> listeners = Lists.newCopyOnWriteArrayList();
	
	
	public BaseLifeCycle(LifeCycle lifecycle){
		this.lifecycle = lifecycle;
	}

	public void addLifeCycleListener(LifeCycleListener listener){
		if(listener == null){
			return;
		}else{
			listeners.add(listener);
		}
	}

	public List<LifeCycleListener> getListeners() {
		return listeners;
	}

	public void fireLifeCycleEvent(LifeCycle.LifeCyclePhase phase,Object data){
		if(listeners.size()>0){
			for(LifeCycleListener listener:listeners){
				listener.lifeCycleEvent(new LifeCycleEvent(lifecycle, phase, data));
			}
		}
	}
}
