package org.nchc.test;

import org.nchc.command.CreateNetwork;
import org.nchc.command.CreateSwitch;

public class TestFunction {
	
	public static void main(String [] args) {
		
		//test createNetwork
		CreateNetwork ctn = new CreateNetwork("NETWORKCREATE","211.73.95.50","6633");
		ctn.setCommand();
		System.out.println("command:"+ctn.getCommand());	
		
		String test = "Virtual network has been created (network_id {u'mask': 16, u'isBooted': False, u'networkAddress': 167772160, u'controllerUrls': [u'tcp:211.73.95.36:6633'], u'tenantId': 2}).";
		String [] splitString = test.split("u'tenantId': ");
		String networkid = String.valueOf(splitString[1].charAt(0));
		ctn.setNetworkID(Integer.parseInt(networkid));
		System.out.println("networkID:"+ctn.getNetworkID());
		
		
		//test CreateSwitch	
		CreateSwitch cts = new CreateSwitch("CREATESWITCH", "cc:4e:24:c4:20:84:00:00", "cc:4e:24:c4:45:7c:00:00");
		cts.setCommand();
		System.out.println("command:"+cts.getCommand());
		
		test = "Virtual switch has been created (tenant_id 2, switch_id 00:a4:23:05:00:00:00:01)";
		String substringkey = "switch_id ";
		int swidStartIndex = test.indexOf(substringkey) + substringkey.length();
		String teststring = test.substring( swidStartIndex ,(test.length()-1));	
		cts.setVSwitchID(teststring);
		System.out.println("vswitchID:"+cts.getVSwitchID());
		
		
		//test CreatPort	
	//	CreatePort sctp = new CreatePort("CREATEPORT", "cc:4e:24:c4:20:84:00:00", "2", 8);
	//	CreatePort dctp = new CreatePort("CREATEPORT", "cc:4e:24:c4:45:7c:00:00", "2", 8);
	//	sctp.setCommand();
	//	dctp.setCommand();
	//	System.out.println("command1:"+sctp.getCommand());
	//	System.out.println("command2:"+dctp.getCommand());
		
		
		test = "Virtual port has been created (tenant_id 2, switch_id 00:a4:23:05:00:00:00:01, port_id 1, tag 8)";
		substringkey = "port_id ";
		swidStartIndex = test.indexOf(substringkey) + substringkey.length();
		teststring = test.substring( swidStartIndex , test.lastIndexOf(","));	
	//	sctp.setvport(teststring);
	//	System.out.println("svport:"+sctp.getvport());
		
		test = "Virtual port has been created (tenant_id 2, switch_id 00:a4:23:05:00:00:00:01, port_id 2, tag 8)";
		substringkey = "port_id ";
		swidStartIndex = test.indexOf(substringkey) + substringkey.length();
		teststring = test.substring( swidStartIndex , test.lastIndexOf(","));	
	//	dctp.setvport(teststring);
	//	System.out.println("dvport:"+dctp.getvport());
		
	}

}
