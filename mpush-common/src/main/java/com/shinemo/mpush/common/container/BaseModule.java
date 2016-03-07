package com.shinemo.mpush.common.container;

import com.shinemo.mpush.common.container.manager.ModuleManager;
import com.shinemo.mpush.tools.spi.ServiceContainer;


public abstract class BaseModule implements Module{

	private static final Module moduleManager = ServiceContainer.getInstance(Module.class, "moduleManager");
	
	public BaseModule(Listener listener) {
		ModuleManager manager = (ModuleManager)moduleManager;
		manager.register(this, listener);
	}
	
}
