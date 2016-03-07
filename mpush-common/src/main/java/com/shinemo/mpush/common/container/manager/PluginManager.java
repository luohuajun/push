package com.shinemo.mpush.common.container.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.shinemo.mpush.common.container.Plugin;

public class PluginManager {
	
	private static final Logger log = LoggerFactory.getLogger(PluginManager.class);
	
	private List<Plugin> plugins = Lists.newArrayList();
	
	public void register(Plugin plugin){
		if(StringUtils.isBlank(plugin.getName())){
			throw new IllegalArgumentException("plugin name is not null");
		}
		plugins.add(plugin);
		log.error("register plugin:"+plugin.getName());
	}
	
	public void init(){
		for(Plugin plugin:plugins){
			plugin.init();
			log.error("plugin init:{}",plugin.getName());
		}
	}
	
	public void destroy(){
		for(Plugin plugin:plugins){
			plugin.destroy();
			log.error("plugin destroy:{}",plugin.getName());
		}
	}

}
