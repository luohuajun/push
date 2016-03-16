package com.shinemo.mpush.common.conn;

import com.shinemo.mpush.api.Server.Listener;
import com.shinemo.mpush.api.spi.SPI;

@SPI("connectionServerManage")
public interface ConnectionServerManage {
	
	public void init();
	
	public void stop();
	
	public void start();

	public void initListener(final Listener listener);
	
}
