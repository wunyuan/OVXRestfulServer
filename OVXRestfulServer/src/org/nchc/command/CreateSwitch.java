package org.nchc.command;


/*
 *		This is a "create switch command" function. In order to set command and other important attributes for creating virtual switch. 
 *	
 *		Author: wunyuan Huang(NCHC)
 * 		
 */

public class CreateSwitch extends Command {
	
	private String srcSwitchID;
	private String dstSwitchID;
	private String vSwtichID;
	
	public CreateSwitch(String type, String srcSwitchID, String dstSwitchID) {
		
		super(type);
		this.srcSwitchID = srcSwitchID;
		this.dstSwitchID = dstSwitchID;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setCommand() {
		// TODO Auto-generated method stub
		String middleCommand = "createSwitch " + CreateSwitch.networkID + " ";
		String lastCommand = "";
		if(switchidCompare(this.srcSwitchID, this.dstSwitchID))
			lastCommand = this.srcSwitchID;
		else
			lastCommand = this.srcSwitchID + "," + this.dstSwitchID ;
		this.command = this.command + middleCommand  + lastCommand;		
	}
	
	private boolean switchidCompare(String src, String dst){
		if(src.equals(dst))
			return true;
		else
			return false;
	}
	
	public void setVSwitchID( String vSwitchID) {
		// TODO Auto-generated method stub
		this.vSwtichID = vSwitchID;
	}

	public String getVSwitchID() {
		return this.vSwtichID;
	}
}
