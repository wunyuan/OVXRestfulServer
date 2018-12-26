
package org.nchc.restService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.restlet.data.Protocol;


/*
 *		This is the program entrance.
 *	
 *		Author: wunyuan Huang(NCHC)
 * 		
 */
public class HttpsServer {
    public static void main(String[] args) throws Exception {
    	
    	
    	String vlanEnable = "1";
    	
    	Properties prop = new Properties();
    	
    	try{
    		
        	prop.load(new FileInputStream("./config/config.properties"));
        	
    	}catch (FileNotFoundException ex) {
    		System.out.println(ex.toString());
    		
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	Configuration.httpPort = (prop.getProperty("http_port") != null) ? prop.getProperty("http_port") : Configuration.httpPort ;
    	Configuration.controllIP = (prop.getProperty("controller_ip") != null) ? prop.getProperty("controller_ip") : Configuration.controllIP ;
    	Configuration.controllPort = (prop.getProperty("controller_port") != null) ? prop.getProperty("controller_port") : Configuration.controllPort ;
    	Configuration.ipDomain = (prop.getProperty("ip_domain") != null) ? prop.getProperty("ip_domain") : Configuration.ipDomain ;
    	Configuration.subnetMask = (prop.getProperty("subnet_mask") != null) ? prop.getProperty("subnet_mask") : Configuration.subnetMask ;
    	Configuration.ovxctlPath = (prop.getProperty("ovxctl_path") != null) ? prop.getProperty("ovxctl_path") : Configuration.ovxctlPath ;
    	Configuration.vlanBlock = (prop.getProperty("vlan_block") != null) ? prop.getProperty("vlan_block") : Configuration.vlanBlock ;
    	vlanEnable = (prop.getProperty("vlan_enable") != null) ? prop.getProperty("vlan_enable") : vlanEnable ;
    	
    	Configuration.controllIP = cutSpace(Configuration.controllIP);
    	if(Configuration.vlanBlock != "") {
    		Configuration.vlanBlock = cutSpace(Configuration.vlanBlock);
    	}    	
    	
    	switch (vlanEnable) {
    	
    	case "0":
    		Configuration.vlanEnable = false;
    		break;
    		
    	case "1":
    		Configuration.vlanEnable = true;
    		break;
    		
    	default :
    		Configuration.vlanEnable =true;
    	
    	}
    	  	
    	RestComponent component = new RestComponent();  	
    	component.getServers().add(Protocol.HTTP, Integer.valueOf(Configuration.httpPort));      
        component.start();
        
    }
    
    private static String cutSpace( String str) {
    	return str.replaceAll("\\s+", "");
    }
    
}
