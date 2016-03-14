package com.shinemo.mpush.conn.client;

import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.tools.MPushUtil;


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
