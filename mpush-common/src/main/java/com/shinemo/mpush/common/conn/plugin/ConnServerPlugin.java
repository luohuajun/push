package com.shinemo.mpush.common.conn.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.Server;
import com.shinemo.mpush.api.container.LifeCycle.LifeCyclePhase;
import com.shinemo.mpush.api.container.LifeCycleEvent;
import com.shinemo.mpush.api.container.LifeCycleListener;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.conn.ConnectionServerManage;
import com.shinemo.mpush.common.zk.ZkManage;
import com.shinemo.mpush.tools.Jsons;

public class ConnServerPlugin implements LifeCycleListener{

	private ConnectionServerManage connectionServerManage = ServiceContainer.getInstance(ConnectionServerManage.class, "connectionServerManage");
	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage");
	private static final Logger log = LoggerFactory.getLogger(ConnServerPlugin.class);
	
	@Override
	public void lifeCycleEvent(LifeCycleEvent event) {
		if(event.getPhase().equals(LifeCyclePhase.BEFORE_START)){
			
			final Application application = (Application)event.getData();
			
			Server.Listener startListener = new Server.Listener() {
				
				@Override
				public void onSuccess() {
					registerServerToZk(application.getServerRegisterZkPath(), Jsons.toJson(application));
					log.error("mpush app start "+ application.getPort() +", success...");
				}
				
				@Override
				public void onFailure(String message) {
					log.error("mpush app start "+ application.getPort()+" failuer...");
					System.exit(-1);
				}
				
			};
			
			connectionServerManage.initListener(startListener);
			
		}
	}
	
	public void registerServerToZk(String path,String value){
		zkManage.registerEphemeralSequential(path, value);
        log.error("register server to zk:{},{}",path,value);
	}
	
}
