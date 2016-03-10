package com.shinemo.mpush.common.container;

import com.shinemo.mpush.common.spi.SPI;


@SPI("")
public interface Plugin {
	
	void init();
	
	void destroy();
	
	String getName();

}
