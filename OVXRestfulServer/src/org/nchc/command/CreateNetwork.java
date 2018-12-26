package org.nchc.command;

import org.nchc.restService.Configuration;

/*
 *		This is a "create network command" function. In order to set command and other important attributes for creating virtual network. 
 *	
 *		Author: wunyuan Huang(NCHC)
 * 		
 */
public class CreateNetwork extends Command {

	private String controllIP;
	private String controllPort;
	
	public CreateNetwork(String type, String controllIP, String controllPort) {
		super(type);
		this.controllIP = controllIP;
		this.controllPort = controllPort;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setCommand() {
		// TODO Auto-generated method stub
		String middlecommand = "createNetwork tcp:"; 
		String lastcommand = controllIP + ":" + controllPort + " " + Configuration.ipDomain + " " + Configuration.subnetMask;
		this.command = this.command + middlecommand + lastcommand;	
	}
	
}
