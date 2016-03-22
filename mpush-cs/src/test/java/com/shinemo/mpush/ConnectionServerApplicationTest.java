package com.shinemo.mpush;

import org.junit.Test;

import com.shinemo.mpush.common.Application;
import com.shinemo.mpush.tools.Jsons;

public class ConnectionServerApplicationTest {
	
	@Test
	public void testJson() throws Exception{
		
		Application application = new Application();
		
		String str = Jsons.toJson(application);
		
		System.out.println(str);
		
	}

}
