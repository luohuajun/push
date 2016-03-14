package com.shinemo.mpush.common.dns.manage;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.common.dns.DnsMapping;
import com.shinemo.mpush.common.dns.DnsMappingManage;

public class DnsMappingManageImpl implements DnsMappingManage{
	
	private static final Logger LOG = LoggerFactory.getLogger(DnsMappingManageImpl.class);

	private Map<String, List<DnsMapping>> all = Maps.newConcurrentMap();
	private Map<String, List<DnsMapping>> available = Maps.newConcurrentMap();

	@Override
	public void init() {
		LOG.error("start init dnsmapping");
		all.putAll(ConfigCenter.holder.dnsMapping());
		available.putAll(ConfigCenter.holder.dnsMapping());
		LOG.error("end init dnsmapping");
	}

	@Override
	public void update(Map<String, List<DnsMapping>> nowAvailable) {
		available = nowAvailable;
	}

	@Override
	public Map<String, List<DnsMapping>> getAll() {
		return all;
	}

	@Override
	public DnsMapping translate(String origin) {
		if (available.isEmpty())
			return null;
		List<DnsMapping> list = available.get(origin);
		if (list == null || list.isEmpty())
			return null;
		int L = list.size();
		if (L == 1)
			return list.get(0);
		return list.get((int) (Math.random() * L % L));
	}

}
