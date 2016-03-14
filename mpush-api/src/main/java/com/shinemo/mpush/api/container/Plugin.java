package com.shinemo.mpush.api.container;

import com.shinemo.mpush.api.spi.SPI;



@SPI("")
public interface Plugin {
	
	void init();
	
	void destroy();
	
	String getName();

}
