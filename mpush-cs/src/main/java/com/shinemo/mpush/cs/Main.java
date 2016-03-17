package com.shinemo.mpush.cs;

import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.monitor.service.MonitorDataCollector;

public class Main {

	public static void main(String[] args) {
		final ModuleManage moduleManage = new ModuleManage();
		moduleManage.start();
		
		//开启监控
		MonitorDataCollector.start(ConfigCenter.holder.skipDump());
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	moduleManage.stop();
            }
        });
	}
	
}
