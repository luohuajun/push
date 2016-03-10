package com.shinemo.mpush.tools.thread.threadpool;

import java.util.concurrent.Executor;


public interface ThreadPool {
	public Executor getExecutor(ThreadPoolContext context);
}
