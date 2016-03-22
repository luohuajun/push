package com.shinemo.mpush.common.gateway;

import com.shinemo.mpush.api.connection.Connection;

public interface GatewayServerManage {
	
	public Connection getConnection(String ipAndPort);

}
