//package com.shinemo.mpush.push.manage.impl;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Map;
//
//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.common.collect.Maps;
//import com.shinemo.mpush.api.Client;
//import com.shinemo.mpush.api.Server.Listener;
//import com.shinemo.mpush.api.connection.Connection;
//import com.shinemo.mpush.common.Application;
//import com.shinemo.mpush.common.ServerManage;
//import com.shinemo.mpush.conn.client.GatewayServerApplication;
//import com.shinemo.mpush.netty.client.NettyClient;
//import com.shinemo.mpush.netty.client.NettyClientFactory;
//import com.shinemo.mpush.push.client.ClientChannelHandler;
//
//public class GatewayServerManage implements ServerManage{
//
//	private static final Logger log = LoggerFactory.getLogger(GatewayServerManage.class);
//
//	private static Map<String,Application> holder = Maps.newConcurrentMap();
//	
//	private final Map<Application, Client> application2Client = Maps.newConcurrentMap();
//    
//    private final Map<String,Client> ip2Client = Maps.newConcurrentMap();
//	
//	@Override
//	public void addOrUpdate(String fullPath,final Application application){
//		holder.put(fullPath, application);
//		try{
//			Client client = new NettyClient(application.getIp(), application.getPort());
//			ClientChannelHandler handler = new ClientChannelHandler(client);
//			NettyClientFactory.INSTANCE.create(handler);
//			application2Client.put(application, client);
//			ip2Client.put(application.getIp(), client);
//		}catch(Exception e){
//			log.error("addOrUpdate:{},{}",fullPath,application,e);
//		}
//		printList();
//	}
//	
//	@Override
//	public void remove(String fullPath){
//		Application application = get(fullPath);
//		if(application!=null){
//			Client client = application2Client.get(application);
//			if(client!=null){
//				client.stop();
//			}
//		}
//		ip2Client.remove(application.getIp()+":"+application.getPort());
//		holder.remove(fullPath);
//		printList();
//	}
//	
//	@Override
//	public Collection<Application> getList() {
//		return Collections.unmodifiableCollection(holder.values());
//	}
//	
//	private void printList(){
//		for(Application app:holder.values()){
//			log.warn(ToStringBuilder.reflectionToString(app, ToStringStyle.DEFAULT_STYLE));
//		}
//	}
//	
//	public Application get(String fullpath){
//		return holder.get(fullpath);
//	}
//
//	public Client getClient(GatewayServerApplication application){
//		return application2Client.get(application);
//	}
//	
//	public Connection getConnection(String ipAndPort) {
//        Client client = ip2Client.get(ipAndPort);
//        if (client == null) return null;
//        return client.getConnection();
//    }
//
//	@Override
//	public void init(Listener listener, Application application) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void stop() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void start() {
//		// TODO Auto-generated method stub
//		
//	}
//	
//}
//
