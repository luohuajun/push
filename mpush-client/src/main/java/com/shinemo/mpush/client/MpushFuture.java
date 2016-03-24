package com.shinemo.mpush.client;

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

public class MpushFuture implements Future {

	private static final Logger log = LoggerFactory.getLogger(MpushFuture.class);

	private static final Map<Long, MpushFuture> futures = Maps.newConcurrentMap();

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

	public MpushFuture(Request request, int timeout) {
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
				throw new PushMessageException();
			}
		}
		return null;
	}

	public boolean isDone() {
		return endTime > 0;
	}

	public static void done(long id) {
		
		try {
			MpushFuture future = futures.remove(id);
			if (future != null) {
				log.info("request done:"+id);
				future.doDone();
			} else {
				log.warn("The timeout response:"+id+",finally returned at " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())));
			}
		} catch (Exception e) {
			log.error("done exception:"+id,e);
		}
	}

	public static MpushFuture getFuture(long id) {
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
					for (MpushFuture future : futures.values()) {
						if (future == null || future.isDone()) {
							continue;
						}
						if (System.currentTimeMillis() - future.getStart() > future.getTimeout()) {
							MpushFuture.done(future.id);
						}
					}
					//1秒钟执行一次
					Thread.sleep(1000);
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
