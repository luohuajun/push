package com.shinemo.mpush.common.router.manager;

import com.shinemo.mpush.api.RedisKey;
import com.shinemo.mpush.api.router.RouterManager;
import com.shinemo.mpush.common.router.RemoteRouter;
import com.shinemo.mpush.tools.redis.manage.RedisManage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ohun on 2015/12/23.
 */
public class RemoteRouterManager implements RouterManager<RemoteRouter> {
    public static final Logger LOGGER = LoggerFactory.getLogger(RemoteRouterManager.class);

    @Override
    public RemoteRouter register(String userId, RemoteRouter router) {
    	LOGGER.info("register remote router success userId={}, router={}", userId, router);
    	String key = RedisKey.getUserKey(userId);
        RemoteRouter old = RedisManage.get(key, RemoteRouter.class);
        if (old != null) {
            RedisManage.del(key);
        }
        RedisManage.set(key, router);
        return old;
    }

    @Override
    public boolean unRegister(String userId) {
    	String key = RedisKey.getUserKey(userId);
        RedisManage.del(key);
        LOGGER.info("unRegister remote router success userId={}", userId);
        return true;
    }

    @Override
    public RemoteRouter lookup(String userId) {
    	String key = RedisKey.getUserKey(userId);
        return RedisManage.get(key, RemoteRouter.class);
    }
}
