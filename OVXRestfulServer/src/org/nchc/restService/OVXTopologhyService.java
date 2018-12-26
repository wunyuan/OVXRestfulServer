package org.nchc.restService;

import java.io.IOException;

import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.nchc.command.CommandType;
import org.nchc.command.GetPhysicalTopologyCommand;
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
public class OVXTopologhyService extends ServerResource {
		
	@Get
	//Get topology data
	public String toString() {
		
		GetPhysicalTopologyCommand command = new GetPhysicalTopologyCommand(CommandType.GETTOPOLOGY);
		command.setCommand();
		JSONObject resultJson = callOVXPythonApp(command.getCommand(), command.getType());
		return resultJson.toString();
	   }
	
	
	@Post
	public String postHandle(Representation entity) {			
		
		return "Please Use GET command to get topology information.";

	}
	
	//It can call external application to communicate with Openvirtex.
	@SuppressWarnings("resource")
	private JSONObject callOVXPythonApp(String command, String commandType) {

		String [] result = {"no_data","no_data"};
		String errString = "";
		JSONObject resultJson = null;
		
		try {
	
			//call external application
			
			Process p = Runtime.getRuntime().exec(command);		
			Scanner resultString = new Scanner(p.getInputStream()); 
			Scanner errMsg = new Scanner(p.getErrorStream()); 
			
			if(errMsg.hasNextLine()) {
				
				while(errMsg.hasNextLine()) 
					errString = errString+"\n"+ errMsg.nextLine();
				throw new IOException(errString);	
				
			}
			else {
				
				int index = 0;				
				while(resultString.hasNextLine()) 	{
					
					//result[0] is JSON data string
					result[index] = resultString.nextLine();
					index++;
				}
			}
			
			resultJson = new JSONObject(result[0]);		
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("OVXTopologyService.java JSON Error.");
			e.printStackTrace();
		}
		
			
		System.out.println(resultJson);
		return resultJson;
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
}
