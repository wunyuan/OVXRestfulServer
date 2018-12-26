package org.nchc.command;

public class RemoveNetwork extends Command {
	
	public RemoveNetwork(String commandType) {
		super(commandType);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void setCommand() {
		// TODO Auto-generated method stub
		String middleCommand = "removeNetwork ";
		String lastCommand = String.valueOf(Command.networkID);
		this.command = this.command + middleCommand + lastCommand;
		
	}

}
