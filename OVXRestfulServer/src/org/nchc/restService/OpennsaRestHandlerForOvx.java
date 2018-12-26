package org.nchc.restService;

import java.io.IOException;

import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.nchc.command.CommandType;
import org.nchc.command.CreateNetwork;
import org.nchc.command.CreatePort;
import org.nchc.command.CreateSwitch;
import org.nchc.command.RemoveNetwork;
import org.nchc.command.StartCommand;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


/*
 *		This restful server is Openvirtex restful service. You can use \"POST\" method to ask a vlan between switches. 
 *	
 *		Author: wunyuan Huang(NCHC)
 * 		
 */
public class OpennsaRestHandlerForOvx extends ServerResource {
	
	private String [] srcSwitchIDPortTag = { "default",  "default", "default"};
	private String [] dstSwitchIDPortTag = { "default",  "default", "default"};
	private int vlanID = 0;
	private JSONObject resultJson = new JSONObject();

	
	@Get
	public String toString() {  
	      return "This restful server is Openvirtex restful service. You can use \"POST\" method to ask a vlan between switches.";  
	   }
	
	
	//this function parse the received data sent from Http client.
	private void storeData(JSONObject jsonData) throws JSONException {
		
		this.resultJson.put("srcswid", (String) jsonData.get("srcsw"));
		this.resultJson.put("dstswid", (String) jsonData.get("dstsw"));	
		this.srcSwitchIDPortTag = ((String) jsonData.get("srcsw")).split("/");
		this.dstSwitchIDPortTag = ((String) jsonData.get("dstsw")).split("/");
		int realVlanID = (int) jsonData.get("vlan");
		this.vlanID = Configuration.vlanEnable ? realVlanID : 0 ;	
		this.resultJson.put("vlanID", realVlanID);
	}
	
	
	@Post
	//it received data sent from Http client and call function to ask Openvirtex to create vlan path. 
	//Of course, it also response the result of vlan creation with JSON format
	//
	//received json data : {"dstsw": "cc:4e:24:d1:19:00:00:00/9/1", "vlan": 1779, "command": "provision", 
	//                      "srcsw": "cc:4e:24:d1:12:80:00:00/2/1"}
	//
	public Representation postHandle(Representation entity) {
		String received = null;
		String [] result;
				
		try {
			received = entity.getText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		JSONObject postData;
		try {
			postData = new JSONObject(received);
			
			switch( (String)postData.get("command") ) {
			
			case "provision":
				
				storeData(postData);
				
				//ask Openvirtex to create a virtual network .
				CreateNetwork ctn = new CreateNetwork(CommandType.CREATENETWORK, Configuration.controllIP, Configuration.controllPort);
				ctn.setCommand();
				result = callOVXPythonApp(ctn.getCommand(), ctn.getType() );
				//After virtual network created, we get vnetwork ID and store it.
				ctn.setNetworkID(Integer.parseInt(result[1]));		
				addJsonData(ctn.getType(), result);
				
				
				//ask Openvirtex to create a virtual switch. 
				CreateSwitch cts = new CreateSwitch(CommandType.CREATESWITCH, this.srcSwitchIDPortTag[0], this.dstSwitchIDPortTag[0]);
				cts.setCommand();
				result = callOVXPythonApp(cts.getCommand(), cts.getType() );
				//After virtual network created, we get vswitch ID and store it.
				cts.setVSwitchID(result[1]);
				addJsonData(cts.getType(), result);
				
				//ask Openvirtex to create a virtual port. 
				CreatePort sctp = new CreatePort(CommandType.CREATEPORT, this.srcSwitchIDPortTag[0], this.srcSwitchIDPortTag[1], this.srcSwitchIDPortTag[2], this.vlanID);
				sctp.setCommand();
				result = callOVXPythonApp(sctp.getCommand(), sctp.getType() );
				//After virtual network created, we get vport ID and store it.
				sctp.setvport(result[1]);
				addJsonData(sctp.getType(), result);
				
				CreatePort dctp = new CreatePort(CommandType.CREATEPORT, this.dstSwitchIDPortTag[0], this.dstSwitchIDPortTag[1], this.dstSwitchIDPortTag[2], this.vlanID);
				dctp.setCommand();
				result = callOVXPythonApp(dctp.getCommand(), dctp.getType() );
				dctp.setvport(result[1]);
				addJsonData(dctp.getType(), result);
				
				//ask Openvirtex to start the virtual port. 
				StartCommand startSrcPort = new StartCommand(CommandType.STARTPORT, cts.getVSwitchID(), String.valueOf(ctn.getNetworkID()), sctp.getvport());
				startSrcPort.setCommand();
				result = callOVXPythonApp(startSrcPort.getCommand(), startSrcPort.getType());
				addJsonData(startSrcPort.getType(), result);
				
				StartCommand startDstPort = new StartCommand(CommandType.STARTPORT, cts.getVSwitchID(), String.valueOf(ctn.getNetworkID()), dctp.getvport());
				startDstPort.setCommand();
				result = callOVXPythonApp(startDstPort.getCommand(), startDstPort.getType());
				addJsonData(startDstPort.getType(), result);
				
				//ask Openvirtex to start the virtual network. 
				StartCommand startvNetwork = new StartCommand(CommandType.STARTNETWORK, String.valueOf(ctn.getNetworkID()));
				startvNetwork.setCommand();
				result = callOVXPythonApp(startvNetwork.getCommand(), startvNetwork.getType());
				addJsonData(startvNetwork.getType(), result);	
				this.resultJson.put("commandType", "provision");
				break;		
				
				//ask Openvirtex to remove network. 
			case "remove":
				
				System.out.println(postData.toString());
				String vNetWorkID = (String)postData.get("vnetworkid");
				RemoveNetwork rmn = new RemoveNetwork(CommandType.REMOVENETWORK);
				rmn.setNetworkID(Integer.parseInt(vNetWorkID));
				rmn.setCommand();
				result = callOVXPythonApp(rmn.getCommand(), rmn.getType());
				addJsonData(rmn.getType(), result);	
				this.resultJson.put("commandType", "remove");
				break;
			
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		

		Representation resultJson = new JsonRepresentation(this.resultJson);
		
		return resultJson;

	}
	
	//It can call external application to communicate with Openvirtex.
	@SuppressWarnings("resource")
	private String[] callOVXPythonApp(String command, String commandType) {

		String [] result = {"no_data", "no_data"};
		String substringkey = "no_data";
		String errString = "";
		
		try {
	
			//call external application
			
			Process p = Runtime.getRuntime().exec(command);		
			System.out.println(command);
			Scanner resultString = new Scanner(p.getInputStream()); 
			Scanner errMsg = new Scanner(p.getErrorStream()); 
			
			if(errMsg.hasNextLine()) {
				
				while(errMsg.hasNextLine()) 
					errString = errString+"\n"+ errMsg.nextLine();
				throw new IOException(errString);	
				
			}
			else {
								
				while(resultString.hasNextLine()) 	{
					
					//put the result of execution into result[0]
					result[0] = resultString.nextLine();			
				}

				//According commandType to handle result message
				switch(commandType) {
				
				case CommandType.CREATENETWORK:	
					
					//Get vnetworkid from result string
					substringkey = "u'tenantId': ";
					String [] splitString = result[0].split(substringkey);	
					String vnetworkid = getIntegerWithinString(splitString[1]);
					result[1] = String.valueOf(vnetworkid);						
					break;
					
				case CommandType.CREATESWITCH:
					
					//Get vswitchid from result string
					substringkey = "switch_id ";
					int swidStartIndex = result[0].indexOf(substringkey) + substringkey.length();
					result[1] = result[0].substring( swidStartIndex ,(result[0].length()-1));				
					break;
					
				case CommandType.CREATEPORT:
					
					//Get vsportid from result string
					substringkey = "port_id ";
					//swidStartIndex = result[0].indexOf(substringkey) + substringkey.length();
					//String [] splitString = ;	
					//result[1] = result[0].substring( swidStartIndex , result[0].lastIndexOf(","));
					result[1] = getIntegerWithinString((result[0].split(substringkey))[1]);
					break;
				
				default :
					result[1] = commandType;
				}
								
			}
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		System.out.println(result[0]);
		return result;
	};	
	
	
	public String getIntegerWithinString(String data) {
		String result = "";
		for(char check : data.toCharArray()){					
			if(Character.isDigit(check))
				result =result+ Character.toString(check);
			else
				break;		
		}
		return result;
	}
	
	
	//It can create JSON format data.
	private JSONObject addJsonData(String commandType ,String [] data) throws JSONException {
		
		int repeatName = 0;
		String jsonName = commandType.toLowerCase();
		
		//check whether JSON name have the same or not. If the JSON name existed in resultJson, JSON name will be add number to it.
		while(this.resultJson.has(jsonName))
		{
			repeatName++;
			jsonName = jsonName + String.valueOf(repeatName);
		}
			
		switch(commandType) {
		
		case CommandType.CREATENETWORK:
			
			String vNetworkIdJsonName = "vnetworkid";
			
			//If the vnetworkid name existed in resultJson, vnetworkid number will add 1.			
			if(repeatName > 0) 
				vNetworkIdJsonName = vNetworkIdJsonName + String.valueOf(repeatName);
			
			this.resultJson.put(jsonName, data[0]);	
			this.resultJson.put(vNetworkIdJsonName,data[1] );
			
			break;
			
		case CommandType.CREATESWITCH:
			
			String vSwitchIdJsonName = "vswitchid";
			
			//If the vswitchid name existed in resultJson, vswitchid will be add number to it.
			if(repeatName > 0)
				vSwitchIdJsonName = vSwitchIdJsonName + String.valueOf(repeatName);
			
			this.resultJson.put((jsonName), data[0]);	
			this.resultJson.put(vSwitchIdJsonName,data[1] );
			
			break;
			
		case CommandType.CREATEPORT:
			
			String vPortIdJsonName = "vportid";
			
			//If the repeatName name existed in resultJson, repeatName will be add number to it.
			if(repeatName > 0)
				vPortIdJsonName = vPortIdJsonName + String.valueOf(repeatName);
		
			this.resultJson.put((jsonName), data[0]);	
			this.resultJson.put(vPortIdJsonName,data[1] );
			
			break;
		
		default :
			
			this.resultJson.put((jsonName), data[0]);	
			
		}
		return this.resultJson;
	}	
}
