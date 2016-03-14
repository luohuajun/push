package com.shinemo.mpush.push.zk.listener.impl;


import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.common.zk.listener.AbstractDataChangeListener;
import com.shinemo.mpush.conn.client.ConnectionServerApplication;

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
