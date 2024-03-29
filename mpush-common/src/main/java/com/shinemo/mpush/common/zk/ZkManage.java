package com.shinemo.mpush.common.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;

import com.shinemo.mpush.api.spi.SPI;
import com.shinemo.mpush.common.zk.listener.DataChangeListener;


@SPI("zkManage")
public interface ZkManage {

	public void init(ZkConfig config);

	public void close();

	public void remove(String key);

	public void registerEphemeralSequential(String key);

	public void registerEphemeralSequential(String key, String value);

	public void registerEphemeral(String key, String value);

	public void update(String key, String value);

	public void registerPersist(String key, String value);

	public boolean isExisted(String key);

	public List<String> getChildrenKeys(String key);

	public String get(String key);

	public CuratorFramework getClient();

	public TreeCache getCache();

	public void registerListener(DataChangeListener listener);

	public ZkConfig getZkConfig();
	
}
