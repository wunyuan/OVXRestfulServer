package org.nchc.command;

import org.nchc.restService.Configuration;

/*
 *		This is a "create port command" function. In order to set command and other important attributes for creating virtual port. 
 *	
 *		Author: wunyuan Huang(NCHC)
 * 		
 */

public class CreatePort extends Command {
	
	private String switchID;
	private String port;
	private String vlanID;
	private String vport;
	
	public CreatePort(String type, String switchID, String port, String tag, int vlanID) {
		
		super(type);
		this.switchID = switchID;
		this.port = port;
		
		switch(tag) {
		
		case "0" :
			this.vlanID = "0" ;
			break;
		case "1" :
			this.vlanID = Integer.toString(vlanID);
			break;
		default :
			System.out.println("The tag value is not a 0 or 1, we assume you want to tag a vlan ID.");
			this.vlanID = Integer.toString(vlanID);	
		}
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setCommand() {
		// TODO Auto-generated method stub
		String [] blockArray = null;
		if(Configuration.vlanBlock != "") {
			blockArray = Configuration.vlanBlock.split(",");
		}
		boolean block = false;
		for(int i=0; i < blockArray.length; i++) {
			if( (this.switchID).equals(blockArray[i])) {
				block = true;
				break;
			}
		}
		
		if(block) {
			this.vlanID="0";
		}
		String middleCommand = "createPort " + CreatePort.networkID + " ";
		String lastCommand = this.switchID + " " + this.port + " "+ this.vlanID ;
		this.command = this.command + middleCommand  + lastCommand;		
	}
	
	public void setvport( String vport) {
		// TODO Auto-generated method stub
		this.vport = vport;
	}

	public String getvport() {
		return this.vport;
	}

}
