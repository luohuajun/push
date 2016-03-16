package com.shinemo.mpush.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.Server;
import com.shinemo.mpush.api.container.BaseLifeCycle;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.zk.ZkManage;
import com.shinemo.mpush.tools.Jsons;

public abstract class BaseServerModule extends BaseLifeCycle{
	
	private static final Logger log = LoggerFactory.getLogger(BaseServerModule.class);
	private ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class, "zkManage");
	
	private Server.Listener startListener = new Server.Listener() {
		
		@Override
		public void onSuccess() {
			zkManage.registerEphemeralSequential(getApplication().getServerRegisterZkPath(), Jsons.toJson(getApplication()));
			log.error("mpush app start "+ getApplication().getPort() +", success...");
		}
		
		@Override
		public void onFailure(String message) {
			log.error("mpush app start "+ getApplication().getPort()+" failuer...");
			System.exit(-1);
		}
		
	};
	
	public abstract Application getApplication();
	
	public Server.Listener getListener(){
		return startListener;
	}

}
