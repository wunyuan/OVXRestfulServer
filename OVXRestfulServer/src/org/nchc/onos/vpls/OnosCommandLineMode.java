package org.nchc.onos.vpls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.nchc.restService.Configuration;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class OnosCommandLineMode {
	
	
	private Session session = null;
	
	
	public abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive{
		
		
		public String getPassword(){ return null; }
		public boolean promptYesNo(String str){ return true; }
		public String getPassphrase(){ return null; }
		public boolean promptPassphrase(String message){ return false; }
		public boolean promptPassword(String message){ return false; }
		public void showMessage(String message){ }
		public String[] promptKeyboardInteractive(String destination,
                        String name,
                        String instruction,
                        String[] prompt,
                        boolean[] echo) {
			return null;
		}
	}
	
	
	public OnosCommandLineMode(String account, String pwd, String ip, int port) {
		
		 try{
		      JSch jsch=new JSch();

		      session=jsch.getSession(account, ip, port);
		      session.setPassword(pwd);

		      UserInfo ui = new MyUserInfo(){};

		      session.setUserInfo(ui);
		      
		    }
		    catch(Exception e){
		      System.out.println(e);
		    }
		
	}
	
	
	public void createVPLS(String [] srcAndPort, String [] dstAndPort, int vlanID ) {		  		 
		 
		 String command = "";
		  
		 try {	  
			 
			 session.connect();

			 System.out.println("app deactivate org.onosproject.fwd");
			 sendCommand("app deactivate org.onosproject.fwd");
			 Thread.sleep(100);
			 
			 System.out.println("app activate org.onosproject.vpls");
			 sendCommand("app activate org.onosproject.vpls");
			 Thread.sleep(100);
			 
			 command = createIfAddCommand(srcAndPort[0], srcAndPort[1], 0, "opennsah1");
			 System.out.println(command);
			 sendCommand(command);
			 Thread.sleep(500);
			 
		     command = createIfAddCommand(dstAndPort[0], dstAndPort[1], 0, "opennsah2");
		     System.out.println(command);
		     sendCommand(command);
		     Thread.sleep(500);
		     
		     
		     System.out.println("vpls create nothing");
		     sendCommand("vpls create nothing");
		     Thread.sleep(500);
		     
		     System.out.println("vpls delete nothing");
		     sendCommand("vpls delete nothing");
		     Thread.sleep(500);
		     
		     System.out.println("vpls create opennsa");
		     sendCommand("vpls create opennsa");
		     Thread.sleep(500);
		     
		     System.out.println("vpls set-encap opennsa VLAN "+vlanID);
		     sendCommand("vpls set-encap opennsa VLAN "+vlanID);
		     Thread.sleep(500);
		     
		     command = createvplsAddIfCommand("opennsa", "opennsah1");
		 //    String command1 = "vpls add-if VPLS5 dh";
		     System.out.println(command);
		     sendCommand(command);
		     Thread.sleep(500);
		     
		     command = createvplsAddIfCommand("opennsa", "opennsah2");
		  //   String command2 = "vpls add-if VPLS5 tp";
		     System.out.println(command);
		     sendCommand(command);
		     Thread.sleep(500);
		     

		     
		    
		 
		 } catch (JSchException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		    session.disconnect();
		 } 
		 
	      session.disconnect();
		  
	 }
	
	public void removeVpls() {
		
		try {
			session.connect();
			System.out.println("vpls delete opennsa");
			sendCommand("vpls delete opennsa");
		} catch (JSchException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.disconnect();
		}
		
		session.disconnect();
		
	}
	
	
	private void sendCommand(String command) throws JSchException, IOException {
		  
		  
		  Channel channel= this.session.openChannel("exec");

	      ((ChannelExec)channel).setCommand(command);

	      channel.connect();     
	      printData(channel);
	      channel.disconnect();
		  
	  }
	
	 
	  
	 private void printData( Channel channel) throws IOException {
		  	  
		  BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
	      String test = null;
	      while( (test= br.readLine()) != null ) {
	    	  System.out.println(test);
	    	  
	      }
		  
	 }
	 
	 public Session getSession() {
			return session;
	 }
		 
		 
	private String createIfAddCommand( String dpid, String port, int vlanID, String name) {
				
			 String command = null;
			 
			 
			 String [] blockArray = null;
			 
			 if(Configuration.vlanBlock != "") {
				 blockArray = Configuration.vlanBlock.split(",");
			 }
			 
			 
			 boolean block = false;
			 for(int i=0; i < blockArray.length; i++) {
				 if( (dpid).equals(blockArray[i])) {
					 block = true;
					 break;
				 }
			 }
				
			 if(block) {
				 vlanID=0;
			 }
			 	 
			 String [] dpidSplit = dpid.split(":");
			 
			 StringBuilder sb = new StringBuilder();
			 for(int i = 0; i < dpidSplit.length; i++) {
				 sb.append(dpidSplit[i]);
			 }
			 dpid = sb.toString();		 
			 if( vlanID == 0 ) {				 
				 command = "interface-add of:" + dpid + "/" + port + " " + name;				 
			 }
			 else {			 
				 command = "interface-add -v "+ vlanID +" of:" + dpid + "/" + port + " " + name;			 
			 }
				
			 return command;
	}
	
	
	private String createvplsAddIfCommand(String vplsName, String nodeName) {
		
		return "vpls add-if " + vplsName + " " + nodeName;
		
	}
	  
	  

}
