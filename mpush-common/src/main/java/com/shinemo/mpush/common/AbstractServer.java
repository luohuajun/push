package com.shinemo.mpush.common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.shinemo.mpush.api.Server;
import com.shinemo.mpush.common.app.Application;
import com.shinemo.mpush.tools.GenericsUtil;
import com.shinemo.mpush.tools.Jsons;
import com.shinemo.mpush.tools.config.ConfigCenter;
import com.shinemo.mpush.tools.redis.RedisGroup;
import com.shinemo.mpush.tools.spi.ServiceContainer;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;
import com.shinemo.mpush.tools.zk.ZKPath;
import com.shinemo.mpush.tools.zk.ZkRegister;
import com.shinemo.mpush.tools.zk.listener.DataChangeListener;
import com.shinemo.mpush.tools.zk.listener.impl.RedisPathListener;

public abstract class AbstractServer<T extends Application> {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractServer.class);
	
    protected Application application;
    
    protected List<DataChangeListener> dataChangeListeners = Lists.newArrayList();
    
    protected ZkRegister zkRegister = ServiceContainer.getInstance(ZkRegister.class);
    
    protected Server server;
    
	public AbstractServer() {
		this.application = getApplication();
		registerListener(new RedisPathListener());
	}
	
	@SuppressWarnings("unchecked")
	private Application getApplication() {
		try {
			return ((Class<Application>) GenericsUtil.getSuperClassGenericType(this.getClass(), 0)).newInstance();
		} catch (Exception e) {
			log.error("exception:",e);
			throw new RuntimeException(e);
		}
	}

	public abstract Server getServer();
	
	public void registerListener(DataChangeListener listener){
		dataChangeListeners.add(listener);
	}

	//step1 启动 zk
	private void initZK(){
    	zkRegister.init();
	}
	
	//step2 获取redis
	private void initRedis(){
		boolean exist = zkRegister.isExisted(ZKPath.REDIS_SERVER.getPath());
		String rawGroup = zkRegister.get(ZKPath.REDIS_SERVER.getPath());
        if (!exist||Strings.isNullOrEmpty(rawGroup)) {
            List<RedisGroup> groupList = ConfigCenter.holder.redisGroups();
            zkRegister.registerPersist(ZKPath.REDIS_SERVER.getPath(), Jsons.toJson(groupList));
        }
        //强刷
        boolean forceWriteRedisGroupInfo = ConfigCenter.holder.forceWriteRedisGroupInfo();
        if(forceWriteRedisGroupInfo){
        	List<RedisGroup> groupList = ConfigCenter.holder.redisGroups();
            zkRegister.registerPersist(ZKPath.REDIS_SERVER.getPath(), Jsons.toJson(groupList));
        }
	}
	
	//step3 注册listener
	private void registerListeners(){
		for(DataChangeListener listener:dataChangeListeners){
			zkRegister.registerListener(listener);
		}
	}
	
	//step4 初始化 listener data
	private void initListenerData(){
		for(DataChangeListener listener:dataChangeListeners){
			listener.initData();
		}
	}
	
	//step5 初始化server
	private void initServer(){
		server = getServer();
	}
	
	public void startServer(final Server server){
		this.startServer(server, null, null);
	}
	
	//step6 启动 netty server
	public void startServer(final Server server,final String path, final String value){
		Runnable runnable = new Runnable() {
            @Override
            public void run() {
                server.init();
                server.start(new Server.Listener() {
                    @Override
                    public void onSuccess() {
                        log.error("mpush app start "+server.getClass().getSimpleName()+" server success....");
                        if(StringUtils.isNoneBlank(path)&&StringUtils.isNoneBlank(value)){
                        	registerServerToZk(path,value);
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                    	log.error("mpush app start "+server.getClass().getSimpleName()+" server failure, jvm exit with code -1");
                        System.exit(-1);
                    }
                });
            }
        };
        ThreadPoolManager.newThread(server.getClass().getSimpleName(), runnable).start();
        
	}
	
	//step7  注册应用到zk
	public void registerServerToZk(String path,String value){
        zkRegister.registerEphemeralSequential(path, value);
        log.error("register server to zk:{},{}",path,value);
	}
	
	public void start(){
		initZK();
		initRedis();
		registerListeners();
		initListenerData();
		initServer();
		startServer(server,application.getServerRegisterZkPath(),Jsons.toJson(application));
//		registerServerToZk(application.getServerRegisterZkPath(),Jsons.toJson(application));
	}
	
	public void startClient(){
		initZK();
		initRedis();
		registerListeners();
		initListenerData();
	}
	
	public void stopServer(Server server){
		if(server!=null){
			server.stop(null);
		}
	}
	
	public void stop(){
		stopServer(server);
	}
	
}
