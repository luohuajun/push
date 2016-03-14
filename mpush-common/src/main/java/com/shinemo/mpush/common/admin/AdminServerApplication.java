package com.shinemo.mpush.common.admin;

import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.tools.MPushUtil;

public class AdminServerApplication extends Application{
	
	public AdminServerApplication() throws Exception {
		this(ConfigCenter.holder.adminPort(),ZKPath.ADMIN_SERVER.getWatchPath(),MPushUtil.getLocalIp(),MPushUtil.getExtranetAddress());
	}
	
	public AdminServerApplication(int port,String path,String ip,String extranetIp) {
		setPort(port);
		setServerRegisterZkPath(path);
		setIp(ip);
		setExtranetIp(extranetIp);
	}

}
