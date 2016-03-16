package com.shinemo.mpush.common.conn;

import com.shinemo.mpush.api.Server.Listener;
import com.shinemo.mpush.api.spi.SPI;
import com.shinemo.mpush.common.Application;

@SPI("connectionServerManage")
public interface ConnectionServerContainer {
	
	public void init();
	
	public void stop();
	
	public void start();

	public void initListener(final Listener listener);
	
	public void initApplication(Application application);
	
}
