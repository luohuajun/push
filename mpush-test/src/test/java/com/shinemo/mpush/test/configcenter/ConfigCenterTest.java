package com.shinemo.mpush.test.configcenter;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.common.dns.DnsMapping;
import com.shinemo.mpush.tools.Jsons;

public class ConfigCenterTest {
	
	@Test
	public void test(){
		
		System.out.println(ConfigCenter.holder.forceWriteRedisGroupInfo());
		
	}
	
	@Test
	public void testDnsMapping(){
		Map<String, List<DnsMapping>> ret = ConfigCenter.holder.dnsMapping();
		
		System.out.println(Jsons.toJson(ret));
		
	}

}
