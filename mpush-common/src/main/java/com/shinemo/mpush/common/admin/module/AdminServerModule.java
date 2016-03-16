package com.shinemo.mpush.common.admin.module;

import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.BaseServerModule;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;

public class AdminServerModule extends BaseServerModule{
	
	private ServerManage adminServerManage = ServiceContainer.getInstance(ServerManage.class, "adminServerManage");
	private Application application = new Application();
	
	public AdminServerModule(int port,String path,String ip,String extranetIp) {
		application.setPort(port);
		application.setServerRegisterZkPath(path);
		application.setIp(ip);
		application.setExtranetIp(extranetIp);
	}
	
	@Override
	public void start0() {
		adminServerManage.init(getListener(), application);
		Runnable runnable = new Runnable() {
            @Override
            public void run() {
            	adminServerManage.start();
            }
        };
        ThreadPoolManager.newThread("mpush-"+this.getClass().getSimpleName(), runnable).start();
	}
	
	@Override
	public void stop0() {
		adminServerManage.stop();
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
