package com.shinemo.mpush.common.container;

import java.util.List;

import com.google.common.collect.Lists;

public class BaseLifecycle{
	
	private Lifecycle lifecycle;
	private List<LifecycleListener> listeners = Lists.newCopyOnWriteArrayList();
	
	
	public BaseLifecycle(Lifecycle lifecycle){
		this.lifecycle = lifecycle;
	}

	public void addLifecycleListener(LifecycleListener listener){
		if(listener == null){
			return;
		}else{
			listeners.add(listener);
		}
	}

	public List<LifecycleListener> getListeners() {
		return listeners;
	}

	public void fireLifecycleEvent(Lifecycle.LifecyclePhase phase,Object data){
		if(listeners.size()>0){
			for(LifecycleListener listener:listeners){
				listener.lifecycleEvent(new LifecycleEvent(lifecycle, phase, data));
			}
		}
	}
}
