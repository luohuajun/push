package com.shinemo.mpush.common.conn.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.Server;
import com.shinemo.mpush.api.container.BaseLifeCycle;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.conn.ConnectionServerManage;
import com.shinemo.mpush.common.zk.ZkManage;
import com.shinemo.mpush.tools.Jsons;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;


public class ConnServerModule extends BaseLifeCycle{
	
	private ConnectionServerManage connectionServerManage = ServiceContainer.getInstance(ConnectionServerManage.class, "connectionServerManage");
	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage");
	private Application application = new Application();
	
	private static final Logger log = LoggerFactory.getLogger(ConnServerModule.class);
	
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
	
	public ConnServerModule(int port,String path,String ip,String extranetIp) {
		application.setPort(port);
		application.setServerRegisterZkPath(path);
		application.setIp(ip);
		application.setExtranetIp(extranetIp);
	}
	
	@Override
	public void start0() {
		connectionServerManage.init(startListener, application);
		Runnable runnable = new Runnable() {
            @Override
            public void run() {
            	connectionServerManage.start();
            }
        };
        ThreadPoolManager.newThread(this.getClass().getSimpleName(), runnable).start();
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
