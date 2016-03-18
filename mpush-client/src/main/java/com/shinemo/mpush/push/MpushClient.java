package com.shinemo.mpush.push;

import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.config.module.ConfigCenterModule;
import com.shinemo.mpush.common.conn.module.ConnServerModule;
import com.shinemo.mpush.common.gateway.module.GatewayServerModule;
import com.shinemo.mpush.common.redis.module.RedisModule;
import com.shinemo.mpush.common.zk.module.ZkModule;

public class MpushClient{
	
	private String zkIpLists;
	private String namespace;
	private String digest;
	
	private ZkModule zkModule;
	private RedisModule redisModule;
	private ConfigCenterModule configCenterModule;
	private ConnServerModule connServerModule;
	private GatewayServerModule gatewayServerModule;
	
	private ServerManage connServerManage = ServiceContainer.getInstance(ServerManage.class, "connectionServerManage");
	private ServerManage gatewayServerManage = ServiceContainer.getInstance(ServerManage.class, "gatewayServerManage");
	
    public MpushClient(String zkIpLists,String namespace,String digest) {
    	zkModule = new ZkModule(zkIpLists,namespace,digest);
    	redisModule = new RedisModule();
    	configCenterModule = new ConfigCenterModule();
    	connServerModule = new ConnServerModule();
    	gatewayServerModule = new GatewayServerModule();
    }
    
    public void init(){
    	zkModule.start();
    	redisModule.start();
    	configCenterModule.start();
    	connServerModule.start();
    	gatewayServerModule.start();
    }
    
    public void stop(){
    	configCenterModule.stop();
    	connServerModule.stop();
    	gatewayServerModule.stop();
    	redisModule.stop();
    	zkModule.stop();
    }
    
	public String getZkIpLists() {
		return zkIpLists;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getDigest() {
		return digest;
	}

	public ZkModule getZkModule() {
		return zkModule;
	}

	public RedisModule getRedisModule() {
		return redisModule;
	}

	public ConfigCenterModule getConfigCenterModule() {
		return configCenterModule;
	}

	public ConnServerModule getConnServerModule() {
		return connServerModule;
	}

	public ServerManage getConnServerManage() {
		return connServerManage;
	}

	public GatewayServerModule getGatewayServerModule() {
		return gatewayServerModule;
	}

	public ServerManage getGatewayServerManage() {
		return gatewayServerManage;
	}
	
}
