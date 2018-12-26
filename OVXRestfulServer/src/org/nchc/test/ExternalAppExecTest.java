package org.nchc.test;
/**
 * This program enables you to connect to sshd server and get the shell prompt.
 *   $ CLASSPATH=.:../build javac Shell.java 
 *   $ CLASSPATH=.:../build java Shell
 * You will be asked username, hostname and passwd. 
 * If everything works fine, you will get the shell prompt. Output may
 * be ugly because of lacks of terminal-emulation, but you can issue commands.
 *
 */
import com.jcraft.jsch.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ExternalAppExecTest{
  public static void main(String[] arg){
    
    try{
      JSch jsch=new JSch();

      Session session=jsch.getSession("karaf", "localhost", 8101);
      session.setPassword("karaf");

      UserInfo ui = new MyUserInfo(){};


      session.setUserInfo(ui);


      session.connect(30000);   // making a connection with timeout.

         
      sendCommand(session, "apps -a -s");
      sendCommand(session, "app activate org.onosproject.vpls");
      
      
      session.disconnect();
    }
    catch(Exception e){
      System.out.println(e);
    }
  }
  
  private static void printData( Channel channel) throws IOException {
	  
	  
	  BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
      String test = null;
      while( (test= br.readLine()) != null ) {
    	  System.out.println(test);
    	  
      }
	  
  }
  
  private static void sendCommand(Session session, String command) throws JSchException, IOException {
	  
	  
	  Channel channel=session.openChannel("exec");

      ((ChannelExec)channel).setCommand(command);

      channel.connect();     
      printData(channel);
      channel.disconnect();
	  
  }
  

  public static abstract class MyUserInfo
                          implements UserInfo, UIKeyboardInteractive{
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
                                              boolean[] echo){
      return null;
    }
  }
}