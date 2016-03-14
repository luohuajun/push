package com.shinemo.mpush.common.redis;

import java.util.List;

import com.shinemo.mpush.api.spi.SPI;



@SPI("redisManage")
public interface RedisManage {

	public void init(List<RedisGroup> group);

	public List<RedisGroup> getGroupList();

	public RedisNode randomGetRedisNode(String key);

	public List<RedisNode> hashSet(String key);
	
	public void close();
	
	public void init();

}
