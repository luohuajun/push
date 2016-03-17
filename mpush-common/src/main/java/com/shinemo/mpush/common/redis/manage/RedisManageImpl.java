package com.shinemo.mpush.common.redis.manage;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.shinemo.mpush.common.redis.RedisGroup;
import com.shinemo.mpush.common.redis.RedisManage;
import com.shinemo.mpush.common.redis.RedisNode;
import com.shinemo.mpush.log.LogType;
import com.shinemo.mpush.log.LoggerManage;
import com.shinemo.mpush.tools.Jsons;

public class RedisManageImpl implements RedisManage{

    private static List<RedisGroup> groups = Lists.newArrayList();

    /**
     * zk 启动的时候需要调用这个
     */
    @Override
    public void init(List<RedisGroup> group) {
        if (group == null || group.isEmpty()) {
        	LoggerManage.log(LogType.REDIS, "init redis client error, redis server is none.");
            throw new RuntimeException("init redis client error, redis server is none.");
        }
        groups = group;
        printGroupList("init");
    }


    @Override
    public List<RedisGroup> getGroupList() {
        return Collections.unmodifiableList(groups);
    }

    private void printGroupList(String reason) {
        for (RedisGroup app : groups) {
        	LoggerManage.log(LogType.REDIS,reason+","+Jsons.toJson(app));
        }
    }

    public int groupSize() {
        return groups.size();
    }

    /**
     * 随机获取一个redis 实例
     *
     * @param key
     * @return
     */
    @Override
    public RedisNode randomGetRedisNode(String key) {
        int size = groupSize();
        if (size == 1) return groups.get(0).get(key);
        int i = (int) ((Math.random() % size) * size);
        RedisGroup group = groups.get(i);
        return group.get(key);
    }

    /**
     * 写操作的时候，获取所有redis 实例
     *
     * @param key
     * @return
     */
    @Override
    public List<RedisNode> hashSet(String key) {
        List<RedisNode> nodeList = Lists.newArrayList();
        for (RedisGroup group : groups) {
            RedisNode node = group.get(key);
            nodeList.add(node);
        }
        return nodeList;
    }


	@Override
	public void close() {
	}


	@Override
	public void init() {
		
	}

}
