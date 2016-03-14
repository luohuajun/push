package com.shinemo.mpush.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		final ConnectionServerMain connectionServerMain = new ConnectionServerMain();
		connectionServerMain.start();
		
		//开启监控
//		MonitorDataCollector.start(ConfigCenter.holder.skipDump());
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	connectionServerMain.stop();
            	log.warn("connection stop success!");
            }
        });
	}

}
