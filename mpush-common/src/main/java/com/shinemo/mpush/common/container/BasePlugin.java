package com.shinemo.mpush.common.container;

import com.shinemo.mpush.common.container.manager.PluginManager;
import com.shinemo.mpush.tools.spi.ServiceContainer;


public abstract class BasePlugin implements Plugin{

	private static final Plugin pluginManager = ServiceContainer.getInstance(Plugin.class, "pluginManager");
	
	public BasePlugin() {
		PluginManager manager = (PluginManager)pluginManager;
		manager.register(this);
	}
	
}
