package com.shinemo.mpush.common.dns.module;

import com.shinemo.mpush.api.container.BaseLifeCycle;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.dns.DnsMappingManage;
import com.shinemo.mpush.common.dns.plugin.DnsMappingPlugin;

public class DnsModule extends BaseLifeCycle{
	
	private DnsMappingManage dnsMappingManage = ServiceContainer.getInstance(DnsMappingManage.class, "dnsMappingManage"); 

	public DnsModule() {
		addLifeCycleListener(new DnsMappingPlugin());
	}
	
	@Override
	public void start0() {
		dnsMappingManage.init();
	}
	
}
