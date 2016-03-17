package com.shinemo.mpush.core.util;

import com.shinemo.mpush.common.config.ConfigCenter;

public class MPushUtil {

	public static int getHeartbeat(int min, int max) {
		return Math.max(ConfigCenter.holder.minHeartbeat(), Math.min(max, ConfigCenter.holder.maxHeartbeat()));
	}

}
