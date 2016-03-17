package com.shinemo.mpush.push;

import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.config.module.ConfigCenterModule;
import com.shinemo.mpush.common.conn.module.ConnServerModule;
import com.shinemo.mpush.common.redis.module.RedisModule;
import com.shinemo.mpush.common.zk.module.ZkModule;

public class ConnClient{

	private String zkIpLists;
	private String namespace;
	private String digest;
	private int connPort;
	
	private ZkModule zkModule = new ZkModule("127.0.0.1:2181","mpush-daily","shinemoIpo");
	
	private RedisModule redisModule = new RedisModule();
	
	private ConfigCenterModule configCenterModule = new ConfigCenterModule();
	
	private ConnServerModule connServerModule = new ConnServerModule();
	
	private ServerManage connServerManage = ServiceContainer.getInstance(ServerManage.class, "connectionServerManage");
	
    public ConnClient() {
    	
    	
    	
    }

}
