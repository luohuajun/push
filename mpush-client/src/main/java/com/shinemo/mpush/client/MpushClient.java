package com.shinemo.mpush.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import com.shinemo.mpush.api.Future.Callback;
import com.shinemo.mpush.api.connection.Connection;
import com.shinemo.mpush.api.exception.PushMessageException;
import com.shinemo.mpush.api.router.ClientLocation;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.config.module.ConfigCenterModule;
import com.shinemo.mpush.common.conn.module.ConnServerModule;
import com.shinemo.mpush.common.gateway.GatewayServerManage;
import com.shinemo.mpush.common.gateway.module.GatewayServerModule;
import com.shinemo.mpush.common.message.domain.GatewayPushMessage;
import com.shinemo.mpush.common.redis.module.RedisModule;
import com.shinemo.mpush.common.router.RemoteRouter;
import com.shinemo.mpush.common.router.manager.ConnectionRouterManager;
import com.shinemo.mpush.common.zk.module.ZkModule;

public class MpushClient {

	private String zkIpLists;
	private String namespace;
	private String digest;

	private ZkModule zkModule;
	private RedisModule redisModule;
	private ConfigCenterModule configCenterModule;
	private ConnServerModule connServerModule;
	private GatewayServerModule gatewayServerModule;

	private ServerManage connServerManage = ServiceContainer.getInstance(ServerManage.class, "connectionServerManage");
	private ServerManage gatewayServerManage = ServiceContainer.getInstance(ServerManage.class, "gatewayServerManage");
	
	private static Executor pool = Executors.newFixedThreadPool(5);

	public MpushClient(String zkIpLists, String namespace, String digest) {
		zkModule = new ZkModule(zkIpLists, namespace, digest);
		redisModule = new RedisModule();
		configCenterModule = new ConfigCenterModule();
		connServerModule = new ConnServerModule();
		gatewayServerModule = new GatewayServerModule();
	}

	public MpushFuture newRequest(Request request) {
		MpushFuture future = new MpushFuture(request, 0);
		try {
			pool.execute(new Worker(request, this));
		} catch (PushMessageException e) {
			future.cancel();
			throw e;
		}
		return future;
	}
	
	//nothing to return
	public void request(Request request){
		MpushFuture future = newRequest(request);
		future.get();
	}

	public void init() {
		zkModule.start();
		redisModule.start();
		configCenterModule.start();
		connServerModule.start();
		gatewayServerModule.start();
	}

	public void stop() {
		configCenterModule.stop();
		connServerModule.stop();
		gatewayServerModule.stop();
		redisModule.stop();
		zkModule.stop();
	}

	public String getZkIpLists() {
		return zkIpLists;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getDigest() {
		return digest;
	}

	public ZkModule getZkModule() {
		return zkModule;
	}

	public RedisModule getRedisModule() {
		return redisModule;
	}

	public ConfigCenterModule getConfigCenterModule() {
		return configCenterModule;
	}

	public ConnServerModule getConnServerModule() {
		return connServerModule;
	}

	public ServerManage getConnServerManage() {
		return connServerManage;
	}

	public GatewayServerModule getGatewayServerModule() {
		return gatewayServerModule;
	}

	public ServerManage getGatewayServerManage() {
		return gatewayServerManage;
	}

	private void send(final Request request) {
		// 1.查询用户长连接所在的机器
		RemoteRouter router = ConnectionRouterManager.INSTANCE.lookup(request.getUserId());

		final Callback callback = request.getCallback();

		if (router == null) {
			MpushFuture.done(request.getId());
			if (callback != null) {
				callback.onOffline(request.getUserId());
			}
			return;
		}

		// 2.通过网关连接，把消息发送到所在机器
		ClientLocation location = router.getRouteValue();
		Connection gatewayConn = ((GatewayServerManage) gatewayServerManage).getConnection(location.getHost());
		if (gatewayConn == null || !gatewayConn.isConnected()) {
			MpushFuture.done(request.getId());
			if (callback != null) {
				callback.onFailure(request.getUserId());
			}
			return;
		}

		final GatewayPushMessage pushMessage = new GatewayPushMessage(request.getUserId(), request.getContent(), gatewayConn);
		pushMessage.sendRaw(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				MpushFuture.done(request.getId());
				if (future.isSuccess()) {
					if (callback != null) {
						callback.onSuccess(request.getUserId());
					}
				} else {
					if (callback != null) {
						callback.onFailure(request.getUserId());
					}
				}
			}
		});
	}
	
	private static class Worker implements Runnable{
		
		private Request request;
		private MpushClient client;
		Worker(Request request,MpushClient client){
			this.request = request;
			this.client = client;
		}
		
		@Override
		public void run() {
			this.client.send(request);
		}
		
	}

}
