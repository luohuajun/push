package com.shinemo.mpush.common.conn.module;

import com.shinemo.mpush.api.container.BaseLifeCycle;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.conn.ConnectionServerManage;
import com.shinemo.mpush.common.conn.plugin.ConnServerPlugin;


public class ConnServerModule extends BaseLifeCycle{
	
	private ConnectionServerManage connectionServerManage = ServiceContainer.getInstance(ConnectionServerManage.class, "connectionServerManage");
	private Application application = new Application();
	
	public ConnServerModule(int port,String path,String ip,String extranetIp) {
		addLifeCycleListener(new ConnServerPlugin());
		application.setPort(port);
		application.setServerRegisterZkPath(path);
		application.setIp(ip);
		application.setExtranetIp(extranetIp);
	}
	
	@Override
	public void start0() {
		connectionServerManage.init();
		connectionServerManage.initApplication(application);
		connectionServerManage.start();
	}
	
	@Override
	public void stop0() {
		connectionServerManage.stop();
	}
	
	@Override
	public Object getData() {
		return application;
	}
	
}
