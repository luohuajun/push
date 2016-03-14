package com.shinemo.mpush.test.redis;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.redis.RedisGroup;
import com.shinemo.mpush.common.redis.RedisManage;
import com.shinemo.mpush.common.redis.RedisManageUtil;
import com.shinemo.mpush.common.redis.RedisNode;
import com.shinemo.mpush.common.redis.pubsub.Subscriber;

public class PubSubTest {
	
    private RedisManage redisRegister = ServiceContainer.getInstance(RedisManage.class);
    
    @Before
    public void init(){
    	RedisNode node = new RedisNode("127.0.0.1", 6379, "shinemoIpo");
    	RedisGroup group = new RedisGroup();
    	group.addRedisNode(node);
    	List<RedisGroup> listGroup = Lists.newArrayList(group);
    	redisRegister.init(listGroup);
    }
	
	@Test
	public void subpubTest(){
		RedisManageUtil.subscribe(Subscriber.holder, "/hello/123");
		RedisManageUtil.subscribe(Subscriber.holder, "/hello/124");	
		RedisManageUtil.publish("/hello/123", "123");
		RedisManageUtil.publish("/hello/124", "124");
	}
	
	@Test
	public void pubsubTest(){
		RedisManageUtil.publish("/hello/123", "123");
		RedisManageUtil.publish("/hello/124", "124");
		RedisManageUtil.subscribe(Subscriber.holder, "/hello/123");
		RedisManageUtil.subscribe(Subscriber.holder, "/hello/124");	
	}
	
	@Test
	public void pubTest(){
		RedisManageUtil.publish("/hello/123", "123");
		RedisManageUtil.publish("/hello/124", "124");
	}
	
	@Test
	public void subTest(){
		RedisManageUtil.subscribe(Subscriber.holder, "/hello/123");
		RedisManageUtil.subscribe(Subscriber.holder, "/hello/124");	
		LockSupport.park();
	}
	
}
