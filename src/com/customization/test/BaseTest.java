package com.customization.test;

import com.customization.commons.Console;
import org.junit.Before;
import weaver.general.GCONST;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class BaseTest {

	@Before
	public void before() throws Exception {
		GCONST.setServerName("ecology");
		//GCONST.setRootPath("D:\\devIdeaProjects\\懒人DEMO\\web\\");
		GCONST.setRootPath("");


		String hostname = "Unknown";
		try
		{
			InetAddress addr= InetAddress.getLocalHost();
			hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
			System.out.println("Hostname can not be resolved");
		}
		Console.log( hostname);
	}


}
