package com.shinemo.mpush.common.gateway;

import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.tools.MPushUtil;
import com.shinemo.mpush.tools.config.ConfigCenter;

public class GatewayServerApplication extends Application{
	
	public GatewayServerApplication() {
		this(ConfigCenter.holder.gatewayServerPort(),ZKPath.GATEWAY_SERVER.getWatchPath(),MPushUtil.getLocalIp());
	}
	
	public GatewayServerApplication(int port,String path,String ip) {
		setPort(port);
		setServerRegisterZkPath(path);
		setIp(ip);
	}

}
