package org.nchc.command;

/*
 *		This is a startcommand function. In order to set command and other important attribute for start virtual network or virtual port. 
 *	
 *		Author: wunyuan Huang(NCHC)
 * 		
 */
public class StartCommand extends Command {
	
	private String vSwitchID;
	private String vNetworkID;
	private String vPortID;
	
	public StartCommand(String type, String vNetworkID) {
		super(type);
		this.vNetworkID = vNetworkID;
	}

	
	public StartCommand(String type, String vSwitchID, String vNetworkID, String vPortID) {
		super(type);
		this.vSwitchID = vSwitchID;
		this.vNetworkID = vNetworkID;
		this.vPortID = vPortID;
	}
	

	@Override
	public void setCommand() {
		// TODO Auto-generated method stub
		String lastCommand;
		String middleCommand;
		
		switch(this.type) {
			
			case CommandType.STARTPORT :
				
				middleCommand = "startPort ";
				lastCommand = this.vNetworkID+ " " + this.vSwitchID+ " " + this.vPortID ;
				break;
				
			case CommandType.STARTNETWORK :
				
				middleCommand = "startNetwork ";
				lastCommand = this.vNetworkID;
				break;
				
			default :
				middleCommand = "";
				lastCommand = "";			
				
		}
		
		this.command = this.command + middleCommand + lastCommand;
		
	}

}
