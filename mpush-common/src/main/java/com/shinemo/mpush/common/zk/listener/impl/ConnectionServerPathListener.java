package com.shinemo.mpush.common.zk.listener.impl;


import com.shinemo.mpush.common.conn.ConnectionServerApplication;
import com.shinemo.mpush.common.container.ServerManage;
import com.shinemo.mpush.common.spi.ServiceContainer;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.common.zk.listener.AbstractDataChangeListener;

/**
 * connection server 应用  监控
 * 
 */
public class ConnectionServerPathListener extends AbstractDataChangeListener<ConnectionServerApplication>{
	
	@SuppressWarnings("unchecked")
	private ServerManage<ConnectionServerApplication> connectionServerManage = ServiceContainer.getInstance(ServerManage.class);
	
	@Override
	public String listenerPath() {
		return ZKPath.CONNECTION_SERVER.getWatchPath();
	}

	@Override
	public String getRegisterPath() {
		return ZKPath.CONNECTION_SERVER.getPath();
	}

	@Override
	public ServerManage<ConnectionServerApplication> getServerManage() {
		return connectionServerManage;
	}

	@Override
	public String getFullPath(String raw) {
		return ZKPath.CONNECTION_SERVER.getFullPath(raw);
	}

}
