package com.shinemo.mpush.test.connection.mpns;

import java.util.List;

import com.google.common.collect.Lists;
import com.shinemo.mpush.conn.client.ConnectionServerApplication;

public class ConnectionClientMain {


	private static final List<ConnectionServerApplication> applicationLists = Lists.newArrayList(new ConnectionServerApplication(20882,"","111.1.57.148","111.1.57.148"));
	
	public List<ConnectionServerApplication> getApplicationList(){
		return Lists.newArrayList(applicationLists);
	}
}
