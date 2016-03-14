package com.shinemo.mpush;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.container.LifeCycle.LifeCyclePhase;
import com.shinemo.mpush.api.container.LifeCycleEvent;
import com.shinemo.mpush.api.container.LifeCycleListener;
import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.common.config.module.ConfigCenterModule;
import com.shinemo.mpush.common.dns.module.DnsModule;
import com.shinemo.mpush.common.redis.module.RedisModule;
import com.shinemo.mpush.common.zk.module.ZkModule;
import com.shinemo.mpush.monitor.service.MonitorDataCollector;
import com.shinemo.mpush.tools.Jsons;

public class ModuleManage {
	
	private static final Logger log = LoggerFactory.getLogger(ModuleManage.class);
	
	private ZkModule zkModule = new ZkModule();
	
	private RedisModule redisModule = new RedisModule();
	
	private ConfigCenterModule configCenterModule = new ConfigCenterModule();
	
	private DnsModule dnsModule = new DnsModule();
	
	private LifeCycleListener defaultLifeCycleListener = new DefaultLifeCyclyListener();
	
	public ModuleManage() {
		zkModule.addLifeCycleListener(defaultLifeCycleListener);
		redisModule.addLifeCycleListener(defaultLifeCycleListener);
		configCenterModule.addLifeCycleListener(defaultLifeCycleListener);
		dnsModule.addLifeCycleListener(defaultLifeCycleListener);
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
	}
	
	public void stop(){
		zkModule.stop();
		redisModule.stop();
		configCenterModule.stop();
		dnsModule.stop();
	}
	
	public static void main(String[] args) {
		final ModuleManage moduleManage = new ModuleManage();
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
