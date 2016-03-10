package com.shinemo.mpush.zk.listener;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shinemo.mpush.common.app.Application;
import com.shinemo.mpush.common.container.ServerManage;
import com.shinemo.mpush.tools.GenericsUtil;
import com.shinemo.mpush.tools.Jsons;
import com.shinemo.mpush.tools.spi.ServiceContainer;
import com.shinemo.mpush.zk.ZkManage;

public abstract class AbstractDataChangeListener<T extends Application> extends DataChangeListener {
	
	protected static ZkManage zkManage = ServiceContainer.getInstance(ZkManage.class);
	
	private static final Logger log = LoggerFactory.getLogger(AbstractDataChangeListener.class);

	private Class<T> clazz;
	
	@SuppressWarnings("unchecked")
	public AbstractDataChangeListener() {
		clazz = (Class<T>) GenericsUtil.getSuperClassGenericType(this.getClass(), 0);
	}
	
	public void dataChanged(CuratorFramework client, TreeCacheEvent event, String path) throws Exception {
		String data = "";
		if (event.getData() != null) {
			data = ToStringBuilder.reflectionToString(event.getData(), ToStringStyle.MULTI_LINE_STYLE);
		}
		if (Type.NODE_ADDED == event.getType()) {
			dataAddOrUpdate(event.getData(),clazz);
		} else if (Type.NODE_REMOVED == event.getType()) {
			dataRemove(event.getData());
		} else if (Type.NODE_UPDATED == event.getType()) {
			dataAddOrUpdate(event.getData(),clazz);
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
	
	public abstract ServerManage<T> getServerManage();

	private void _initData() {
		// 获取机器列表
		List<String> rawData = zkManage.getChildrenKeys(getRegisterPath());
		for (String raw : rawData) {
			String fullPath = getFullPath(raw);
			T app = getServerApplication(fullPath,clazz);
			getServerManage().addOrUpdate(fullPath, app);
		}
	}

	private void dataRemove(ChildData data) {
		String path = data.getPath();
		getServerManage().remove(path);
	}

	private void dataAddOrUpdate(ChildData data,Class<T> clazz) {
		String path = data.getPath();
		byte[] rawData = data.getData();
		T serverApp = Jsons.fromJson(rawData, clazz);
		getServerManage().addOrUpdate(path, serverApp);
	}

	private T getServerApplication(String fullPath,Class<T> clazz) {
		String rawApp = zkManage.get(fullPath);
		T app = Jsons.fromJson(rawApp,clazz);
		return app;
	}
	
	

}
