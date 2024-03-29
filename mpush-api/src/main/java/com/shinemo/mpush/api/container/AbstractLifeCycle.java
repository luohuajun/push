package com.shinemo.mpush.api.container;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public abstract class AbstractLifeCycle implements LifeCycle{
	
	private static final Logger log = LoggerFactory.getLogger(AbstractLifeCycle.class);
	
	private List<LifeCycleListener> listeners = Lists.newCopyOnWriteArrayList();
	
	private AtomicBoolean startFlag = new AtomicBoolean();
	private AtomicBoolean stopFlag = new AtomicBoolean();
	
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
	
	public Object getData(){
		return null;
	}

	public void fireLifeCycleEvent(LifeCycle.LifeCyclePhase phase){
		if(listeners.size()>0){
			for(LifeCycleListener listener:listeners){
				listener.lifeCycleEvent(new LifeCycleEvent(this, phase, getData()));
			}
		}
	}
	
	@Override
	public void start() {
		if(startFlag.compareAndSet(false, true)){
			fireLifeCycleEvent(LifeCyclePhase.BEFORE_START);
			start0();
			fireLifeCycleEvent(LifeCyclePhase.AFTER_START);
		}else{
			log.error("has start:"+this.getClass().getSimpleName());
		}
	}
	
	public abstract void start0();
	
	public abstract void stop0();
	
	@Override
	public void stop() {
		if(stopFlag.compareAndSet(false, true)){
			fireLifeCycleEvent(LifeCyclePhase.BEFORE_STOP);
			stop0();
			fireLifeCycleEvent(LifeCyclePhase.AFTER_STOP);
		}else{
			log.error("has stop:"+this.getClass().getSimpleName());
		}
	}

	public AtomicBoolean getStartFlag() {
		return startFlag;
	}

	public AtomicBoolean getStopFlag() {
		return stopFlag;
	}
	
}
