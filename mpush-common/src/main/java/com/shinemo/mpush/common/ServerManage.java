package com.shinemo.mpush.common;

import java.util.Collection;

import com.shinemo.mpush.api.spi.SPI;


@SPI("connectionServerManage")
public interface ServerManage<T> {

	public void addOrUpdate(String fullPath, T application);

	public void remove(String fullPath);

	public Collection<T> getList();
	
}
