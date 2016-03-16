package com.shinemo.mpush.common.gateway.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.Server;
import com.shinemo.mpush.api.container.BaseLifeCycle;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.gateway.GatewayServerManage;
import com.shinemo.mpush.common.zk.ZkManage;
import com.shinemo.mpush.tools.Jsons;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;


public class GatewayServerModule extends BaseLifeCycle{
	
	private GatewayServerManage gatewayServerManage = ServiceContainer.getInstance(GatewayServerManage.class, "gatewayServerManage");
	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage");
	private Application application = new Application();
	
	private static final Logger log = LoggerFactory.getLogger(GatewayServerModule.class);
	
	private Server.Listener startListener = new Server.Listener() {
		
		@Override
		public void onSuccess() {
			zkManage.registerEphemeralSequential(application.getServerRegisterZkPath(), Jsons.toJson(application));
			log.error("mpush app start "+ application.getPort() +", success...");
		}
		
		@Override
		public void onFailure(String message) {
			log.error("mpush app start "+ application.getPort()+" failuer...");
			System.exit(-1);
		}
		
	};
	
	public GatewayServerModule(int port,String path,String ip,String extranetIp) {
		application.setPort(port);
		application.setServerRegisterZkPath(path);
		application.setIp(ip);
		application.setExtranetIp(extranetIp);
	}
	
	@Override
	public void start0() {
		gatewayServerManage.init(startListener, application);
		Runnable runnable = new Runnable() {
            @Override
            public void run() {
            	gatewayServerManage.start();
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
	
}
