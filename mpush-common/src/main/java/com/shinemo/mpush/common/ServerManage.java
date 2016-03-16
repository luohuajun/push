package com.shinemo.mpush.common;

import java.util.Collection;

import com.shinemo.mpush.api.Server.Listener;
import com.shinemo.mpush.api.spi.SPI;


@SPI("connectionServerManage")
public interface ServerManage{

	public void addOrUpdate(String fullPath, Application application);

	public void remove(String fullPath);

	public Collection<Application> getList();
	
	public void init(final Listener listener,final Application application);
	
	public void stop();
	
	public void start();
	
}
