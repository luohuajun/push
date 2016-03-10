package com.shinemo.mpush.common.container;

import com.shinemo.mpush.tools.spi.SPI;

@SPI("")
public interface Module {
	
	void init();
	
	void start(Listener listener);
	
	void stop(Listener listener);
	
	String getName();
	
	interface Listener {
        void onSuccess(Module module);
        void onFailure(Module module,String message);
    }
	
}
