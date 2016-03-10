package com.shinemo.mpush.common.container.manager;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.shinemo.mpush.common.container.Module;

public class ModuleManager implements Module{
	
	private static final Logger log = LoggerFactory.getLogger(ModuleManager.class);

	private Map<Module,Listener> modules = Maps.newConcurrentMap();

	public void register(Module module,Listener listener){
		if(StringUtils.isBlank(module.getName())||listener == null){
			throw new IllegalArgumentException("module name or listener is not null");
		}
		modules.put(module,listener);
	}

	public void init() {
		log.error("module map start init");
		for(Entry<Module, Listener> entry : modules.entrySet()){
			Module key = entry.getKey();
			key.init();
			log.error("module:{},has init",key.getName());
		}
	}

	public void start(Listener listener) {
		log.error("module map start start");
		for(Entry<Module, Listener> entry : modules.entrySet()){
			Module key = entry.getKey();
			Listener value = entry.getValue();
			key.start(value);
			log.error("module:{},has start",key.getName());
		}
	}

	public void stop(Listener listener) {
		log.error("module map start stop");
		for(Entry<Module, Listener> entry : modules.entrySet()){
			Module key = entry.getKey();
			Listener value = entry.getValue();
			key.stop(value);
			log.error("module:{},has stop",key.getName());
		}
	}

	@Override
	public String getName() {
		return "moduleManager";
	}
	
}
