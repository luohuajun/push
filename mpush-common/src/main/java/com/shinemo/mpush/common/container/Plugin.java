package com.shinemo.mpush.common.container;

import com.shinemo.mpush.tools.spi.SPI;

@SPI("")
public interface Plugin {
	
	void init();
	
	void destroy();
	
	String getName();

}
