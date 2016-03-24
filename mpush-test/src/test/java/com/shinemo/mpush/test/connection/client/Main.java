package com.shinemo.mpush.test.connection.client;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

import com.google.common.collect.Lists;
import com.shinemo.mpush.api.ChannelClientHandler;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.security.CipherBox;
import com.shinemo.mpush.core.gateway.client.GatewayClientChannelHandler;
import com.shinemo.mpush.netty.client.NettyClientFactory;
import com.shinemo.mpush.netty.client.SecurityNettyClient;

public class Main {

	public static void main(String[] args) throws Exception {

		ConnModuleManage manage = new ConnModuleManage();
		manage.start();
		
		manage.start();
		
		List<Application> serverList = Lists.newArrayList(manage.getServerManage().getList());
		
		int index = (int) ((Math.random() % serverList.size()) * serverList.size());
		Application server = serverList.get(index);

		for(int i = 0;i<1;i++){
			String clientVersion =  "1.0." + i;
			String osName = "android";
			String osVersion = "1.0.1";
			String userId = "huang1-"+i;
			String deviceId = "test-device-id-"+i;
			String cipher = "";
			byte[] clientKey = CipherBox.INSTANCE.randomAESKey();
			byte[] iv = CipherBox.INSTANCE.randomAESIV();
			
			SecurityNettyClient client = new SecurityNettyClient(server.getIp(), server.getPort());
	    	client.setClientKey(clientKey);
	    	client.setIv(iv);
	    	client.setClientVersion(clientVersion);
	    	client.setDeviceId(deviceId);
	    	client.setOsName(osName);
	    	client.setOsVersion(osVersion);
	    	client.setUserId(userId);
	    	client.setCipher(cipher);
			
			ChannelClientHandler handler = new GatewayClientChannelHandler(client);
			NettyClientFactory.INSTANCE.create(handler);
			Thread.sleep(10);
		}
		
		LockSupport.park();

	}

}
