package com.shinemo.mpush.zk;

public class ZkConfig {
	
    private final String ipLists;
    
    private final  String namespace;
    
    private final int maxRetry;
    
    private final int minTime;
    
    private final int maxTime;
    
    private final int sessionTimeout;
    
    private final int connectionTimeout;
    
    private final String digest;
    
    private final String localCachePath;

	public ZkConfig(String ipLists, String namespace) {
		this(ipLists, namespace, null);
	}
	
	public ZkConfig(String ipLists, String namespace,String digest) {
		this(ipLists, namespace, ZkContext.ZK_MAX_RETRY, ZkContext.ZK_MIN_TIME, ZkContext.ZK_MAX_TIME, ZkContext.ZK_SESSION_TIMEOUT, ZkContext.ZK_CONNECTION_TIMEOUT,digest,ZkContext.ZK_DEFAULT_CACHE_PATH);
	}
	
	public ZkConfig(String ipLists, String namespace, int maxRetry, int minTime, int maxTime, int sessionTimeout, int connectionTimeout,String digest,String localCachePath) {
		this.ipLists = ipLists;
		this.namespace = namespace;
		this.maxRetry = maxRetry;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.sessionTimeout = sessionTimeout;
		this.connectionTimeout = connectionTimeout;
		this.digest = digest;
		this.localCachePath = localCachePath;
	}

	public String getIpLists() {
		return ipLists;
	}

	public String getNamespace() {
		return namespace;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public int getMinTime() {
		return minTime;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public String getDigest() {
		return digest;
	}

	public String getLocalCachePath() {
		return localCachePath;
	}
	
}
