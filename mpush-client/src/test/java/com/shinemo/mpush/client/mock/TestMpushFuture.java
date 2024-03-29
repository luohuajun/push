package com.shinemo.mpush.client.mock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.shinemo.mpush.api.Future;
import com.shinemo.mpush.api.exception.PushMessageException;
import com.shinemo.mpush.common.config.ConfigCenter;
import com.shinemo.mpush.tools.thread.threadpool.ThreadPoolManager;

public class TestMpushFuture implements Future {

	private static final Logger log = LoggerFactory.getLogger(TestMpushFuture.class);

	private static final Map<Long, TestMpushFuture> futures = Maps.newConcurrentMap();

	private final long id;

	private final int timeout;

	private final Lock lock = new ReentrantLock();

	private final Condition done = lock.newCondition();

	private final long start = System.currentTimeMillis();

	private volatile long endTime;

	static {
		Thread th = ThreadPoolManager.newThread("MpushRequestTimeoutScanTimer", new MpushRequestTimeoutScan());
		th.setDaemon(true);
		th.start();
	}

	public void cancel() {
		futures.remove(id);
	}

	public TestMpushFuture(TestRequest request, int timeout) {
		this.id = request.getId();
		this.timeout = timeout;
		futures.put(id, this);
	}

	public Object get() {
		return get(timeout);
	}

	// 没什么可以返回的，暂时返回null。
	public Object get(int timeout) {
		if (timeout <= 0) {
			timeout = ConfigCenter.holder.gatewayRequestDefaultTimeout();
		}
		if (!isDone()) {
			long start = System.currentTimeMillis();
			lock.lock();
			try {
				while (!isDone()) {
					done.await(timeout, TimeUnit.MILLISECONDS);
					if (isDone() || System.currentTimeMillis() - start > timeout) {
						break;
					}
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				lock.unlock();
			}

			if (!isDone()) {
				log.error("not done:"+id);
				throw new PushMessageException();
			}
		}
		return null;
	}

	public boolean isDone() {
		return endTime > 0;
	}
	
	public static void finishRequest(long id){
		log.error("finishRequest:"+id);
		try {
			TestMpushFuture future = futures.remove(id);
			if (future != null) {
				future.doDone();
			} else {
				log.warn("The timeout response finally returned at " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())));
			}
		} catch (Exception e) {
			log.error("done exception:"+id,e);
		}
	}

	public static TestMpushFuture getFuture(long id) {
		return futures.get(id);
	}

	private void doDone() {
		lock.lock();
		try {
			endTime = System.currentTimeMillis();
			if (done != null) {
				done.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	private static class MpushRequestTimeoutScan implements Runnable {

		public void run() {
			while (true) {
				try {
					for (TestMpushFuture future : futures.values()) {
						if (future == null || future.isDone()) {
							continue;
						}
						if (System.currentTimeMillis() - future.getStart() > future.getTimeout()) {
							log.error("MpushRequestTimeoutScan:"+future.id);
							TestMpushFuture.finishRequest(future.id);
						}
					}
					Thread.sleep(30);
				} catch (Throwable e) {
					log.error("Exception when scan the timeout.", e);
				}
			}
		}
	}

	public int getTimeout() {
		return timeout;
	}

	public long getStart() {
		return start;
	}

}
