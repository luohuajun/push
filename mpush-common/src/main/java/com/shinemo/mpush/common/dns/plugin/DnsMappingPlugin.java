package com.shinemo.mpush.common.dns.plugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shinemo.mpush.api.container.LifeCycle;
import com.shinemo.mpush.api.container.LifeCycleEvent;
import com.shinemo.mpush.api.container.LifeCycleListener;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.dns.DnsMapping;
import com.shinemo.mpush.common.dns.DnsMappingManage;
import com.shinemo.mpush.tools.Jsons;
import com.shinemo.mpush.tools.MPushUtil;

public class DnsMappingPlugin  implements LifeCycleListener{
	
	private DnsMappingManage dnsMappingManage = ServiceContainer.getInstance(DnsMappingManage.class, "dnsMappingManage"); 
	
	private ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
	
	@Override
	public void lifeCycleEvent(LifeCycleEvent event) {
		if(event.getPhase().equals(LifeCycle.LifeCyclePhase.AFTER_START)){
			Worker worker = new Worker(dnsMappingManage);
			pool.scheduleAtFixedRate(worker, 1, 20, TimeUnit.SECONDS); //20秒 定时扫描dns
		}else if(event.getPhase().equals(LifeCycle.LifeCyclePhase.BEFORE_STOP)){
			pool.shutdown();
		}
	}
	
	public static class Worker implements Runnable {
		
		private static final Logger log = LoggerFactory.getLogger(Worker.class);
		
		private DnsMappingManage dnsMappingManage;
		
		public Worker(DnsMappingManage dnsMappingManage) {
			this.dnsMappingManage = dnsMappingManage;
		}
		
		@Override
		public void run() {

			log.debug("start dns mapping telnet");
			
			Map<String, List<DnsMapping>> all = dnsMappingManage.getAll();

			Map<String, List<DnsMapping>> available = Maps.newConcurrentMap();

			for (Map.Entry<String, List<DnsMapping>> entry : all.entrySet()) {
				String key = entry.getKey();
				List<DnsMapping> value = entry.getValue();
				List<DnsMapping> nowValue = Lists.newArrayList();
				for(DnsMapping temp:value){
					boolean canTelnet = MPushUtil.telnet(temp.getIp(), temp.getPort());
					if(canTelnet){
						nowValue.add(temp);
					}else{
						log.error("dns can not reachable:"+Jsons.toJson(temp));
					}
				}
				available.put(key, nowValue);
			}
			
			dnsMappingManage.update(available);

		}

	}

}
