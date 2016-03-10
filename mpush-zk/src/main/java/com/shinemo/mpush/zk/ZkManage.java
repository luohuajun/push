package com.shinemo.mpush.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;

import com.shinemo.mpush.tools.spi.SPI;
import com.shinemo.mpush.zk.listener.DataChangeListener;


@SPI("zkManage")
public interface ZkManage {

	public void init();

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

	public ZkConfig getZkConfig();

	public TreeCache getCache();

	public void registerListener(DataChangeListener listener);
	
}
