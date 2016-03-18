package com.shinemo.mpush.cs;

import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.monitor.service.MonitorDataCollector;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;

public class Main {

	public static void main(String[] args) {
		final ModuleManage moduleManage = new ModuleManage();
		moduleManage.start();
		
		//开启监控
		MonitorDataCollector.start(ConfigCenter.holder.skipDump());
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				moduleManage.stop();
			}
		};
		
		Thread stop = ThreadPoolManager.newThread("mpush-main", runnable);
		Runtime.getRuntime().addShutdownHook(stop);
	}
	
}
