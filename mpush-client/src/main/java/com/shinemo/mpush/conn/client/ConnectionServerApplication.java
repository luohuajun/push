package com.shinemo.mpush.conn.client;

import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.conn.client.GatewayServerApplication;
import com.shinemo.mpush.tools.MPushUtil;

public class ConnectionServerApplication extends Application{
	
	private transient GatewayServerApplication gatewayServerApplication;
	
	public ConnectionServerApplication() throws Exception {
		this(ConfigCenter.holder.connectionServerPort(),ZKPath.CONNECTION_SERVER.getWatchPath(),MPushUtil.getLocalIp(),MPushUtil.getExtranetAddress());
	}
	
	public ConnectionServerApplication(int port,String path,String ip,String extranetIp) {
		setPort(port);
		setServerRegisterZkPath(path);
		setIp(ip);
		setExtranetIp(extranetIp);
	}

	public GatewayServerApplication getGatewayServerApplication() {
		return gatewayServerApplication;
	}

	public void setGatewayServerApplication(GatewayServerApplication gatewayServerApplication) {
		this.gatewayServerApplication = gatewayServerApplication;
	}

}
