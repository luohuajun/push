package com.shinemo.mpush.api;

import io.netty.channel.ChannelInitializer;

import java.util.concurrent.Executor;

/**
 * Created by ohun on 2015/12/24.
 */
public interface Server {

    void start(Listener listener);

    void stop(Listener listener);

    boolean isRunning();

    interface Listener {
        void onSuccess();

        void onFailure(String message);
    }

	void init(int port, Executor bossExecutor, Executor workExecutor, ChannelInitializer<?> channelInitializer);
}
