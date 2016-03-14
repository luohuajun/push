package com.shinemo.mpush.common.dns;


import java.util.List;
import java.util.Map;

import com.shinemo.mpush.api.spi.SPI;



@SPI("dnsMappingManage")
public interface DnsMappingManage {

	public void init();

	public DnsMapping translate(String origin);

	public void update(Map<String, List<DnsMapping>> nowAvailable);

	public Map<String, List<DnsMapping>> getAll();

	
}
