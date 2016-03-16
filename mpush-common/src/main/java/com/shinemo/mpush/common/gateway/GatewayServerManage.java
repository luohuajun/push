package com.shinemo.mpush.common.gateway;


import com.shinemo.mpush.api.Server.Listener;
import com.shinemo.mpush.api.spi.SPI;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.ServerManage;

@SPI("gatewayServerManage")
public interface GatewayServerManage extends ServerManage<Application>{
	
	public void init(final Listener listener,final Application application);
	
	public void stop();
	
	public void start();

}
