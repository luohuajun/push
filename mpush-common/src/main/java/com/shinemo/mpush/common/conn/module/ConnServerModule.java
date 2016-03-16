package com.shinemo.mpush.common.conn.module;

import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.BaseServerModule;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;

public class ConnServerModule extends BaseServerModule{
	
	private ServerManage connectionServerManage = ServiceContainer.getInstance(ServerManage.class, "connectionServerManage");
	private Application application = new Application();
	
	public ConnServerModule(int port,String path,String ip,String extranetIp) {
		application.setPort(port);
		application.setServerRegisterZkPath(path);
		application.setIp(ip);
		application.setExtranetIp(extranetIp);
	}
	
	@Override
	public void start0() {
		connectionServerManage.init(getListener(), application);
		Runnable runnable = new Runnable() {
            @Override
            public void run() {
            	connectionServerManage.start();
            }
        };
        ThreadPoolManager.newThread("mpush-"+this.getClass().getSimpleName(), runnable).start();
	}
	
	@Override
	public void stop0() {
		connectionServerManage.stop();
	}
	
	@Override
	public Object getData() {
		return application;
	}

	@Override
	public Application getApplication() {
		return application;
	}
	
}
