package com.shinemo.mpush.common.gateway.module;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.BaseServerModule;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.gateway.plugin.GatewayServerPlugin;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;


public class GatewayServerModule extends BaseServerModule{
	private static final Logger log = LoggerFactory.getLogger(GatewayServerModule.class);
	private ServerManage gatewayServerManage = ServiceContainer.getInstance(ServerManage.class, "gatewayServerManage");
	private Application application = new Application();
	
	public GatewayServerModule(int port,String path,String ip,String extranetIp) {
		application.setPort(port);
		application.setServerRegisterZkPath(path);
		application.setIp(ip);
		application.setExtranetIp(extranetIp);
		addLifeCycleListener(new GatewayServerPlugin());
	}
	
	public GatewayServerModule() {
		addLifeCycleListener(new GatewayServerPlugin());
	}
	
	@Override
	public void start0() {
		gatewayServerManage.init(getListener(), application);
		Runnable runnable = new Runnable() {
			private volatile AtomicBoolean startFlag = new AtomicBoolean();
            @Override
            public void run() {
            	if(startFlag.compareAndSet(false, true)){
            		gatewayServerManage.start();
            	}else{
            		log.error("gatewayServerManage thread has stop");
            	}
            }
        };
        ThreadPoolManager.newThread("mpush-"+this.getClass().getSimpleName(), runnable).start();
	}
	
	@Override
	public void stop0() {
		gatewayServerManage.stop();
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
