package com.shinemo.mpush.core.admin.manage;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.core.admin.AdminChannelInitializer;
import com.shinemo.mpush.netty.server.NettyServer;
import com.shinemo.mpush.tools.Jsons;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;

public class AdminServerManageImpl extends NettyServer implements ServerManage{

	private static final Logger log = LoggerFactory.getLogger(AdminServerManageImpl.class);
	
	private Application application;

	private Listener listener;

	private Map<String,Application> holder = Maps.newConcurrentMap();
	
	private AdminChannelInitializer adminChannelInitializer = new AdminChannelInitializer();

	@Override
	public void init(Listener listener,Application application) {
		this.listener = listener;
		this.application = application;
		init(this.application.getPort(), ThreadPoolManager.bossExecutor, ThreadPoolManager.workExecutor, adminChannelInitializer);
	}

	@Override
	public void stop() {
		super.stop(null);
	}

	@Override
	public void start() {
		super.start(listener);
	}

	@Override
	public void addOrUpdate(String fullPath, Application application) {
		if(StringUtils.isNotBlank(fullPath)&&application!=null){
			holder.put(fullPath, application);
		}else{
			log.error("fullPath is null or application is null"+fullPath==null?"fullpath is null":fullPath+","+application==null?"application is null":"application is not null");
		}

		printList();
	}

	@Override
	public void remove(String fullPath){
		holder.remove(fullPath);
		printList();
	}
	
	@Override
	public Collection<Application> getList() {
		return Collections.unmodifiableCollection(holder.values());
	}
	
	private void printList(){
		for(Application app:holder.values()){
			log.warn(Jsons.toJson(app));
		}
	}

	public Application getApplication() {
		return application;
	}
	
}
