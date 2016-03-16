package com.shinemo.mpush.common.zk.listener.impl;


import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.common.zk.listener.AbstractDataChangeListener;

/**
 * connection server 应用  监控
 * 
 */
public class ConnectionServerPathListener extends AbstractDataChangeListener<Application>{
	
	@SuppressWarnings("unchecked")
	private ServerManage<Application> connectionServerManage = ServiceContainer.getInstance(ServerManage.class);
	
	@Override
	public String listenerPath() {
		return ZKPath.CONNECTION_SERVER.getWatchPath();
	}

	@Override
	public String getRegisterPath() {
		return ZKPath.CONNECTION_SERVER.getPath();
	}

	@Override
	public ServerManage<Application> getServerManage() {
		return connectionServerManage;
	}

	@Override
	public String getFullPath(String raw) {
		return ZKPath.CONNECTION_SERVER.getFullPath(raw);
	}

}
