package com.shinemo.mpush.common.conn;


import com.shinemo.mpush.api.Server.Listener;
import com.shinemo.mpush.api.spi.SPI;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.ServerManage;

@SPI("connectionServerManage")
public interface ConnectionServerManage extends ServerManage<Application>{
	
	public void init(final Listener listener,final Application application);
	
	public void stop();
	
	public void start();

}
