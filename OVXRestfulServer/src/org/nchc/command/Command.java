package org.nchc.command;

import org.nchc.restService.Configuration;

public abstract class Command {
	
	protected String command = "python ";
	
	private String options = " -n ";
	
	protected static int networkID = 0;
	
	protected String type;
	
	Command(String type) {
		this.type = type;
		this.command = this.command + Configuration.ovxctlPath + options;
	}
	
	public abstract void setCommand();
	
	public String getCommand() {
		return this.command;
	};
	
	public void setNetworkID(int networkID) {
		Command.networkID = networkID;
	};
	
	public int getNetworkID() {
		return Command.networkID;
	}
	
	public String getType() {
		return this.type;
	}
	
}
