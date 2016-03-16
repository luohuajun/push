package com.shinemo.mpush.core.server;

import com.shinemo.mpush.api.protocol.Command;
import com.shinemo.mpush.common.MessageDispatcher;
import com.shinemo.mpush.core.handler.GatewayPushHandler;
import com.shinemo.mpush.netty.connection.NettyConnectionManager;
import com.shinemo.mpush.netty.server.NettyServer;
import io.netty.channel.ChannelHandler;

/**
 * Created by ohun on 2015/12/30.
 */
public final class GatewayServer extends NettyServer {

    private ServerChannelHandler channelHandler;
    private NettyConnectionManager connectionManager;
    private int port;
    
    public GatewayServer(int port) {
    	this.port = port;
    }

    public void init() {
//        super.init(port);
        MessageDispatcher receiver = new MessageDispatcher();
        receiver.register(Command.GATEWAY_PUSH, new GatewayPushHandler());
        connectionManager = new NettyConnectionManager();
        channelHandler = new ServerChannelHandler(false, connectionManager, receiver);
    }

    @Override
    public void stop(Listener listener) {
        super.stop(listener);
        connectionManager.destroy();
    }

    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }
}
