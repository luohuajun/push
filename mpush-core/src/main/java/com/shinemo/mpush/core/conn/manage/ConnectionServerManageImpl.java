package com.shinemo.mpush.core.conn.manage;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import com.google.common.collect.Maps;
import com.shinemo.mpush.api.connection.ConnectionManager;
import com.shinemo.mpush.api.protocol.Command;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.MessageDispatcher;
import com.shinemo.mpush.common.conn.ConnectionServerManage;
import com.shinemo.mpush.core.handler.BindUserHandler;
import com.shinemo.mpush.core.handler.FastConnectHandler;
import com.shinemo.mpush.core.handler.HandshakeHandler;
import com.shinemo.mpush.core.handler.HeartBeatHandler;
import com.shinemo.mpush.core.handler.HttpProxyHandler;
import com.shinemo.mpush.core.handler.UnbindUserHandler;
import com.shinemo.mpush.core.server.ServerChannelHandler;
import com.shinemo.mpush.netty.client.HttpClient;
import com.shinemo.mpush.netty.client.NettyHttpClient;
import com.shinemo.mpush.netty.connection.NettyConnectionManager;
import com.shinemo.mpush.netty.server.NettyServer;
import com.shinemo.mpush.tools.Jsons;

public class ConnectionServerManageImpl extends NettyServer implements ConnectionServerManage {

	private static final Logger log = LoggerFactory.getLogger(ConnectionServerManageImpl.class);
	
	private Application application;

	private Listener listener;
	private ServerChannelHandler channelHandler;

	private ConnectionManager connectionManager = new NettyConnectionManager();
	private HttpClient httpClient = new NettyHttpClient();
	
	private static Map<String,Application> holder = Maps.newConcurrentMap();

	
	@Override
	public void init(Listener listener,Application application) {
		this.listener = listener;
		this.application = application;
		init(this.application.getPort());
		connectionManager.init();
		MessageDispatcher receiver = new MessageDispatcher();
		receiver.register(Command.HEARTBEAT, new HeartBeatHandler());
		receiver.register(Command.HANDSHAKE, new HandshakeHandler());
		receiver.register(Command.BIND, new BindUserHandler());
		receiver.register(Command.UNBIND, new UnbindUserHandler());
		receiver.register(Command.FAST_CONNECT, new FastConnectHandler());
		receiver.register(Command.HTTP_PROXY, new HttpProxyHandler(httpClient));
		channelHandler = new ServerChannelHandler(true, connectionManager, receiver);
	}

	@Override
	public void stop() {
		super.stop(null);
		httpClient.stop();
		connectionManager.destroy();
	}

	@Override
	public void start() {
		super.start(listener);
	}

	@Override
	public ChannelHandler getChannelHandler() {
		return channelHandler;
	}

	@Override
	protected void initOptions(ServerBootstrap b) {
		super.initOptions(b);
		/***
		 * 你可以设置这里指定的通道实现的配置参数。 我们正在写一个TCP/IP的服务端，
		 * 因此我们被允许设置socket的参数选项比如tcpNoDelay和keepAlive。
		 * 请参考ChannelOption和详细的ChannelConfig实现的接口文档以此可以对ChannelOptions的有一个大概的认识。
		 */
		b.option(ChannelOption.SO_BACKLOG, 1024);

		/**
		 * TCP层面的接收和发送缓冲区大小设置， 在Netty中分别对应ChannelOption的SO_SNDBUF和SO_RCVBUF，
		 * 需要根据推送消息的大小，合理设置，对于海量长连接，通常32K是个不错的选择。
		 */
		b.childOption(ChannelOption.SO_SNDBUF, 32 * 1024);
		b.childOption(ChannelOption.SO_RCVBUF, 32 * 1024);
	}

	@Override
	public void addOrUpdate(String fullPath, Application application) {
		if(StringUtils.isNotBlank(fullPath)&&application!=null){
			holder.put(fullPath, application);
		}else{
			log.error("fullPath is null or application is null"+fullPath==null?"fullpath is null":fullPath+","+application==null?"application is null":"application is not null");
		}

		printList();
	}

	@Override
	public void remove(String fullPath){
		holder.remove(fullPath);
		printList();
	}
	
	@Override
	public Collection<Application> getList() {
		return Collections.unmodifiableCollection(holder.values());
	}
	
	private void printList(){
		for(Application app:holder.values()){
			log.warn(Jsons.toJson(app));
		}
	}
	

}
