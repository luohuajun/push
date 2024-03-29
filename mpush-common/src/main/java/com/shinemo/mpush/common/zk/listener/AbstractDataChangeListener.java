package com.shinemo.mpush.common.zk.listener;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.api.spi.ServiceContainer;
import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.common.ServerManage;
import com.shinemo.mpush.common.zk.ZkManage;
import com.shinemo.mpush.tools.Jsons;

public abstract class AbstractDataChangeListener extends DataChangeListener {
	
	protected static ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class);
	
	private static final Logger log = LoggerFactory.getLogger(AbstractDataChangeListener.class);
	
	public void dataChanged(CuratorFramework client, TreeCacheEvent event, String path) throws Exception {
		String data = "";
		if (event.getData() != null) {
			data = ToStringBuilder.reflectionToString(event.getData(), ToStringStyle.MULTI_LINE_STYLE);
		}
		if (Type.NODE_ADDED == event.getType()) {
			dataAddOrUpdate(event.getData());
		} else if (Type.NODE_REMOVED == event.getType()) {
			dataRemove(event.getData());
		} else if (Type.NODE_UPDATED == event.getType()) {
			dataAddOrUpdate(event.getData());
		} else {
			log.warn(this.getClass().getSimpleName()+"other path:" + path + "," + event.getType().name() + "," + data);
		}
	}
	
	public void initData() {
		log.warn(zkManage.getClient().getNamespace()+" start init "+ this.getClass().getSimpleName()+" server data");
		_initData();
		log.warn(zkManage.getClient().getNamespace()+" end init "+ this.getClass().getSimpleName()+" server data");
	}
	
	public abstract String getRegisterPath();
	
	public abstract String getFullPath(String raw);
	
	public abstract ServerManage getServerManage();

	private void _initData() {
		List<String> rawData = zkManage.getChildrenKeys(getRegisterPath());
		for (String raw : rawData) {
			String fullPath = getFullPath(raw);
			getServerManage().addOrUpdate(fullPath, getServerApplication(fullPath));
		}
	}

	private void dataRemove(ChildData data) {
		String path = data.getPath();
		getServerManage().remove(path);
	}

	private void dataAddOrUpdate(ChildData data) {
		String path = data.getPath();
		byte[] rawData = data.getData();
		Application serverApp = Jsons.fromJson(rawData, Application.class);
		getServerManage().addOrUpdate(path, serverApp);
	}

	private Application getServerApplication(String fullPath) {
		String rawApp = zkManage.get(fullPath);
		Application app = Jsons.fromJson(rawApp,Application.class);
		return app;
	}
	
	

}
