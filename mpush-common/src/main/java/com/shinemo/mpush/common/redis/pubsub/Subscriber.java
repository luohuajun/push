package com.shinemo.mpush.common.redis.pubsub;

import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.redis.listener.MessageListener;
import com.shinemo.mpush.log.LogType;
import com.shinemo.mpush.log.LoggerManage;
import com.shinemo.mpush.tools.Jsons;

import redis.clients.jedis.JedisPubSub;

public class Subscriber extends JedisPubSub {

    private MessageListener dispatcher = ServiceContainer.getInstance(MessageListener.class, "listenerDispatcher");
    
    public static Subscriber holder = new Subscriber();
    
    private Subscriber(){}
    
    @Override
    public void onMessage(String channel, String message) {
    	LoggerManage.log(LogType.REDIS, "onMessage:{},{}", channel,message);
        dispatcher.onMessage(channel, message);
        super.onMessage(channel, message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
    	LoggerManage.log(LogType.REDIS, "onPMessage:{},{},{}",pattern,channel,message);
        super.onPMessage(pattern, channel, message);
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
    	LoggerManage.log(LogType.REDIS, "onPSubscribe:{},{}",pattern,subscribedChannels);
        super.onPSubscribe(pattern, subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    	LoggerManage.log(LogType.REDIS, "onPUnsubscribe:{},{}",pattern,subscribedChannels);
        super.onPUnsubscribe(pattern, subscribedChannels);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
    	LoggerManage.log(LogType.REDIS, "onSubscribe:{},{}",channel,subscribedChannels);
        super.onSubscribe(channel, subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
    	LoggerManage.log(LogType.REDIS, "onUnsubscribe:{},{}",channel,subscribedChannels);
        super.onUnsubscribe(channel, subscribedChannels);
    }


    @Override
    public void unsubscribe() {
    	LoggerManage.log(LogType.REDIS, "unsubscribe");
        super.unsubscribe();
    }

    @Override
    public void unsubscribe(String... channels) {
    	LoggerManage.log(LogType.REDIS, "unsubscribe:{}",Jsons.toJson(channels));
        super.unsubscribe(channels);
    }

}
