package com.shinemo.mpush.common.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.common.container.manager.ModuleManager;
import com.shinemo.mpush.tools.spi.ServiceContainer;


public abstract class BaseModule implements Module{

	private static final Module moduleManager = ServiceContainer.getInstance(Module.class, "moduleManager");
	private static final Logger log = LoggerFactory.getLogger(BaseModule.class);
	
	private static final Listener defaultListener = new DefaultListener();
	
	public BaseModule(Listener listener) {
		if(listener==null){
			listener = defaultListener;
		}
		ModuleManager manager = (ModuleManager)moduleManager;
		manager.register(this, listener);
	}
	
	public static class DefaultListener implements Listener{
		
		@Override
		public void onFailure(Module module, String message) {
			log.error("module start failure:{},message:{}",module.getClass().getSimpleName(),message);
		}

		@Override
		public void onSuccess(Module module) {
			log.error("module start success:{}",module.getClass().getSimpleName());
		}
	}
	
	@Override
	public void start(Listener listener) {
		
	}
	
	@Override
	public void stop(Listener listener) {
		
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
}
