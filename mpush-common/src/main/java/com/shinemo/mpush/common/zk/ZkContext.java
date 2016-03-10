package com.shinemo.mpush.common.zk;

public interface ZkContext {

	int ZK_MAX_RETRY = 3;
	
	int ZK_MIN_TIME = 5000;
	
	int ZK_MAX_TIME = 5000;
	
	int ZK_SESSION_TIMEOUT = 5000;
	
	int ZK_CONNECTION_TIMEOUT = 5000;
	
	String ZK_DEFAULT_CACHE_PATH = "/";
	
}
