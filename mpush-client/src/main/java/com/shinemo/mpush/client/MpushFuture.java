package com.shinemo.mpush.client;

import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.shinemo.mpush.api.Future;

public class MpushFuture implements Future {

	private static final Logger log = LoggerFactory.getLogger(MpushFuture.class);

	private static final Map<Long, MpushFuture> futures = Maps.newConcurrentMap();

	private final long id;
	
	private final int timeout;

	private final Lock lock = new ReentrantLock();

	private final Condition done = lock.newCondition();

	private final long start = System.currentTimeMillis();

	private volatile long sent;

	public MpushFuture(Request request, int timeout) {
		this.id = request.getId();
		this.timeout = timeout;
		futures.put(id, this);
	}

	public Object get() {
		return null;
	}

	public Object get(int timeoutInMillis) {
		return null;
	}

	public void setCallback(Callback callback) {

	}

	public boolean isDone() {
		return false;
	}

}
