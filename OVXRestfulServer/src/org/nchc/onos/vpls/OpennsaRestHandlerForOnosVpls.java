package org.nchc.onos.vpls;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.nchc.restService.Configuration;
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
public class OpennsaRestHandlerForOnosVpls extends ServerResource {
	
	private String [] srcSwitchIDPortTag = { "default", "default", "default"};
	private String [] dstSwitchIDPortTag = { "default", "default", "default"};
	private int vlanID = 0;
	private JSONObject resultJson = new JSONObject();
	
	
	private String onosAccount = "karaf";
	private String onosPwd = "karaf";
	private String onosIp = "localhost";
	private int onosPort = 8101;

	
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
	//it received data sent from Http client and call function to ask Vpls to create vlan path. 
	//Of course, it also response the result of vlan creation with JSON format
	public Representation postHandle(Representation entity) {
		String received = null;
		
		onosIp = Configuration.controllIP;
		onosPort = Integer.valueOf(Configuration.controllPort);
		
		OnosCommandLineMode onos = new OnosCommandLineMode(onosAccount, onosPwd, onosIp, onosPort);
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
				onos.createVPLS(this.srcSwitchIDPortTag, this.dstSwitchIDPortTag, this.vlanID);			
				this.resultJson.put("commandType", "provision");
				this.resultJson.put("status", "success");
				break;		
				
				//ask Openvirtex to remove network. 
			case "remove":	
		//		onos = new OnosCommandLineMode(onosAccount, onosPwd, onosIp, onosPort);
				onos.removeVpls();
				this.resultJson.put("commandType", "remove");
				this.resultJson.put("status", "success");
				break;
			
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		Representation resultJson = new JsonRepresentation(this.resultJson);
		
		return resultJson;

	}
	
	
	
	
	
	
	
}
