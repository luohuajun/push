package com.shinemo.mpush.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.container.LifeCycle.LifeCyclePhase;
import com.shinemo.mpush.api.container.LifeCycleEvent;
import com.shinemo.mpush.api.container.LifeCycleListener;
import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.common.config.module.ConfigCenterModule;
import com.shinemo.mpush.common.conn.module.ConnServerModule;
import com.shinemo.mpush.common.dns.module.DnsModule;
import com.shinemo.mpush.common.gateway.module.GatewayServerModule;
import com.shinemo.mpush.common.redis.module.RedisModule;
import com.shinemo.mpush.common.zk.ZKPath;
import com.shinemo.mpush.common.zk.module.ZkModule;
import com.shinemo.mpush.monitor.service.MonitorDataCollector;
import com.shinemo.mpush.tools.Jsons;
import com.shinemo.mpush.tools.MPushUtil;

public class TestModuleManage {
	
	private static final Logger log = LoggerFactory.getLogger(TestModuleManage.class);
	
	private ZkModule zkModule = new ZkModule(ConfigCenter.holder.zkIp(), ConfigCenter.holder.zkNamespace(),ConfigCenter.holder.zkDigest());
	
	private RedisModule redisModule = new RedisModule();
	
	private ConfigCenterModule configCenterModule = new ConfigCenterModule();
	
	private DnsModule dnsModule = new DnsModule();
	
	private ConnServerModule connServerModule = new ConnServerModule(ConfigCenter.holder.connectionServerPort(),ZKPath.CONNECTION_SERVER.getWatchPath(),MPushUtil.getLocalIp(),MPushUtil.getExtranetAddress());

	private GatewayServerModule gatewayServerModule = new GatewayServerModule(ConfigCenter.holder.gatewayServerPort(),ZKPath.GATEWAY_SERVER.getWatchPath(),MPushUtil.getLocalIp(),MPushUtil.getExtranetAddress());
//	private AdminServerModule adminServerModule = new AdminServerModule();
	
	private LifeCycleListener defaultLifeCycleListener = new DefaultLifeCyclyListener();
	
	public TestModuleManage() {
		zkModule.addLifeCycleListener(defaultLifeCycleListener);
		redisModule.addLifeCycleListener(defaultLifeCycleListener);
		configCenterModule.addLifeCycleListener(defaultLifeCycleListener);
		dnsModule.addLifeCycleListener(defaultLifeCycleListener);
		connServerModule.addLifeCycleListener(defaultLifeCycleListener);
		gatewayServerModule.addLifeCycleListener(defaultLifeCycleListener);
//		adminServerModule.addLifeCycleListener(defaultLifeCycleListener);
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
	
	public void start(){
		zkModule.start();
		redisModule.start();
		configCenterModule.start();
		dnsModule.start();
		connServerModule.start();
		gatewayServerModule.start();
//		adminServerModule.start();	
	}
	
	public void stop(){
		zkModule.stop();
		redisModule.stop();
		configCenterModule.stop();
		dnsModule.stop();
		connServerModule.stop();
		gatewayServerModule.stop();
//		adminServerModule.stop();
	}
	
	public static void main(String[] args) {
		final TestModuleManage moduleManage = new TestModuleManage();
		moduleManage.start();
		
		//开启监控
		MonitorDataCollector.start(ConfigCenter.holder.skipDump());
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	moduleManage.stop();
            }
        });
	}
	
	

}
