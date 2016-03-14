package com.shinemo.mpush.common.config;

import java.lang.reflect.Method;

import org.aeonbits.owner.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.common.redis.RedisGroup;
import com.shinemo.mpush.common.redis.RedisNode;


public class RedisGroupConverter implements Converter<RedisGroup>{

	private static final Logger log = LoggerFactory.getLogger(RedisGroupConverter.class);
	
	@Override
	public RedisGroup convert(Method method, String input) {
		
		log.warn("method:"+method.getName()+","+input);
		

        RedisGroup group = new RedisGroup();
		
		String[] chunks = input.split(",");
        for (String chunk : chunks) {
            String[] entry = chunk.split(":");
            String ip = entry[0].trim();
            String port = entry[1].trim();
            String password = entry[2].trim();
            RedisNode node = new RedisNode(ip, Integer.parseInt(port), password);
            group.addRedisNode(node);
        }
		return group;
	}
	
	

}
