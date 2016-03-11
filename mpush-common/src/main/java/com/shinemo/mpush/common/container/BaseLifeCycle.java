package com.shinemo.mpush.common.container;

import java.util.List;

import com.google.common.collect.Lists;


public class BaseLifeCycle extends AbstractLifeCycle{

	@Override
	public List<LifeCycleListener> getLifeCycleListeners() {
		return Lists.newArrayList();
	}

	@Override
	public void start0() {
		
	}

	@Override
	public void stop0() {
		
	}
	
}
