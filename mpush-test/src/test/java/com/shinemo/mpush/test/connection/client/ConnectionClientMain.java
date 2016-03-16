//package com.shinemo.mpush.test.connection.client;
//
//import java.util.List;
//
//import com.google.common.collect.Lists;
//import com.shinemo.mpush.api.spi.ServiceContainer;
//import com.shinemo.mpush.common.AbstractClient;
//import com.shinemo.mpush.common.ServerManage;
//import com.shinemo.mpush.conn.client.ConnectionServerApplication;
//import com.shinemo.mpush.push.zk.listener.impl.ConnectionServerPathListener;
//
//public class ConnectionClientMain extends AbstractClient {
//
//	@SuppressWarnings("unchecked")
//	private ServerManage<ConnectionServerApplication> connectionServerManage = ServiceContainer.getInstance(ServerManage.class);
//	
//	public ConnectionClientMain() {
//		registerListener(new ConnectionServerPathListener());
//	}
//	
//	public List<ConnectionServerApplication> getApplicationList(){
//		return Lists.newArrayList(connectionServerManage.getList());
//	}
//
//}