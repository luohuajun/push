package com.shinemo.mpush.common.conn.module;

import com.shinemo.mpush.api.container.BaseLifeCycle;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.conn.ConnectionServerManage;
import com.shinemo.mpush.common.conn.plugin.ConnServerPlugin;


public class ConnServerModule extends BaseLifeCycle{
	
	private ConnectionServerManage connectionServerManage = ServiceContainer.getInstance(ConnectionServerManage.class, "connectionServerManage");
	
	public ConnServerModule() {
		addLifeCycleListener(new ConnServerPlugin());
	}
	
	@Override
	public void start0() {
		connectionServerManage.init();
		connectionServerManage.start();
	}
	
	@Override
	public void stop0() {
		connectionServerManage.stop();
	}
	
}
