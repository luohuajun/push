package com.shinemo.mpush.common.conn.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.BaseServerModule;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.conn.plugin.ConnServerPlugin;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;

public class ConnServerModule extends BaseServerModule{
	
	private ServerManage connectionServerManage = ServiceContainer.getInstance(ServerManage.class, "connectionServerManage");
	private Application application = new Application();
	private static final Logger log = LoggerFactory.getLogger(ConnServerModule.class);
	
	public ConnServerModule(int port,String path,String ip,String extranetIp) {
		application.setPort(port);
		application.setServerRegisterZkPath(path);
		application.setIp(ip);
		application.setExtranetIp(extranetIp);
		addLifeCycleListener(new ConnServerPlugin());
	}
	
	public ConnServerModule(){
		addLifeCycleListener(new ConnServerPlugin());
	}
	
	@Override
	public void start0() {
		connectionServerManage.init(getListener(), application);
		Runnable runnable = new Runnable() {
            @Override
            public void run() {
            	log.error("start run connectionServerManage");
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
