package org.nchc.command;

public class GetPhysicalTopologyCommand extends Command {

	public GetPhysicalTopologyCommand(String type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setCommand() {
		// TODO Auto-generated method stub
		String lastCommand = "getPhysicalTopology";
		this.command = this.command + lastCommand;	
	}


}
