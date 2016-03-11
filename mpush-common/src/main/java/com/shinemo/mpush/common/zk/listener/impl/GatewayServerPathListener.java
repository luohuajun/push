package com.shinemo.mpush.common.zk.listener.impl;

import com.shinemo.mpush.common.container.ServerManage;
import com.shinemo.mpush.common.gateway.GatewayServerApplication;
import com.shinemo.mpush.common.spi.ServiceContainer;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.common.zk.listener.AbstractDataChangeListener;

/**
 * gateway server 应用  监控
 * 
 */
public class GatewayServerPathListener extends AbstractDataChangeListener<GatewayServerApplication>{
	
	@SuppressWarnings("unchecked")
	private ServerManage<GatewayServerApplication> gatewayServerManage = ServiceContainer.getInstance(ServerManage.class, "gatewayServerManage");
	
	@Override
	public String listenerPath() {
		return ZKPath.GATEWAY_SERVER.getWatchPath();
	}

	@Override
	public ServerManage<GatewayServerApplication> getServerManage() {
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
