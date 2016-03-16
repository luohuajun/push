package com.shinemo.mpush.common.zk.listener.impl;

import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.common.zk.listener.AbstractDataChangeListener;

/**
 * gateway server 应用  监控
 * 
 */
public class GatewayServerPathListener extends AbstractDataChangeListener{
	
	private ServerManage gatewayServerManage = ServiceContainer.getInstance(ServerManage.class, "gatewayServerManage");
	
	@Override
	public String listenerPath() {
		return ZKPath.GATEWAY_SERVER.getWatchPath();
	}

	@Override
	public ServerManage getServerManage() {
		return gatewayServerManage;
	}

	@Override
	public String getRegisterPath() {
		return ZKPath.GATEWAY_SERVER.getPath();
	}

	@Override
	public String getFullPath(String raw) {
		return ZKPath.GATEWAY_SERVER.getFullPath(raw);
	}

	

}
