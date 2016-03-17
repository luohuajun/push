package com.shinemo.mpush.test.connection.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.container.LifeCycleEvent;
import com.shinemo.mpush.api.container.LifeCycleListener;
import com.shinemo.mpush.api.container.LifeCycle.LifeCyclePhase;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.common.config.module.ConfigCenterModule;
import com.shinemo.mpush.common.conn.module.ConnServerModule;
import com.shinemo.mpush.common.dns.module.DnsModule;
import com.shinemo.mpush.common.redis.module.RedisModule;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.common.zk.module.ZkModule;
import com.shinemo.mpush.tools.Jsons;
import com.shinemo.mpush.tools.MPushUtil;

public class ConnModuleManage {
	
	private static final Logger log = LoggerFactory.getLogger(ConnModuleManage.class);
	
	private ZkModule zkModule = new ZkModule(ConfigCenter.holder.zkIp(), ConfigCenter.holder.zkNamespace(),ConfigCenter.holder.zkDigest());
	
	private RedisModule redisModule = new RedisModule();
	
	private ConfigCenterModule configCenterModule = new ConfigCenterModule();
	
	private DnsModule dnsModule = new DnsModule();
	
	private ConnServerModule connServerModule = new ConnServerModule(ConfigCenter.holder.connectionServerPort(),ZKPath.CONNECTION_SERVER.getWatchPath(),MPushUtil.getLocalIp(),MPushUtil.getExtranetAddress());
	
	private ServerManage connServerManage = ServiceContainer.getInstance(ServerManage.class, "connectionServerManage");
	
	public void start(){
		zkModule.start();
		redisModule.start();
		configCenterModule.start();
		dnsModule.start();
		connServerModule.start();
	}
	
	public void stop(){
		zkModule.stop();
		redisModule.stop();
		configCenterModule.stop();
		dnsModule.stop();
		connServerModule.stop();
	}
	
	public static class DefaultLifeCyclyListener implements LifeCycleListener{
		
		@Override
		public void lifeCycleEvent(LifeCycleEvent event) {
			
			log.error(" default life cycle:"+Jsons.toJson(event));
			
			if(event.getPhase().equals(LifeCyclePhase.BEFORE_START)){
				
			}else if(event.getPhase().equals(LifeCyclePhase.AFTER_START)){
				
			}else if(event.getPhase().equals(LifeCyclePhase.BEFORE_STOP)){
				
			}else if(event.getPhase().equals(LifeCyclePhase.AFTER_STOP)){
				
			}
		}
		
	}
	
	public ServerManage getServerManage(){
		return connServerManage;
	}
	
}
