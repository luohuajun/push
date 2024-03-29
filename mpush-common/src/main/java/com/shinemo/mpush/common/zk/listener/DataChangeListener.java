package com.shinemo.mpush.common.zk.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import com.shinemo.mpush.log.LogType;
import com.shinemo.mpush.log.LoggerManage;

public abstract class DataChangeListener implements TreeCacheListener{

	@Override
	public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
		String path = null == event.getData() ? "" : event.getData().getPath();
        if (path.isEmpty()) {
            return;
        }
        
        if(path.startsWith(listenerPath())){
        	LoggerManage.info(LogType.ZK, "class:{},DataChangeListener:{},{},namespace:{},eventType:{}", this.getClass().getSimpleName(),path,listenerPath(),client.getNamespace(),event.getType().name());
            dataChanged(client, event, path);
        }else{
        	LoggerManage.warn(LogType.ZK, "class:{},DataChangeListener:{},{},namespace:{},eventType:{}", this.getClass().getSimpleName(),path,listenerPath(),client.getNamespace(),event.getType().name());
        }
        
	}
	
	public abstract void initData();
	
	public abstract void dataChanged(CuratorFramework client, TreeCacheEvent event,String path) throws Exception; 
	
	public abstract String listenerPath();
}
