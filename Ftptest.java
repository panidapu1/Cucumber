package mfjobftp;
//************************************************************************
//Ftptest - Used to submit mainframe job from Mainframe Dataset 
// This class will submit a ftp job and which in turn will submit another job	 	
//
// 
//***********************************************************************
/**
 * @author pansr01 
 *
 */
import org.apache.commons.net.ftp.*; 
import org.apache.commons.net.*;
import org.apache.commons.fileupload.*;
import java.io.*;
import java.net.InetAddress;
import java.net.SocketException;
import org.apache.commons.codec.binary.*;

public class Ftptest {
		
	public static void main (String[] args)  { 
	 
	 String testcase = "rq15t001";
	 String jobName = "";
	 String logdir  =  "cmewjobs/" + "dir.txt";
	 //String jobpath = "cmewjobs/GetDir.bat";
     String serverName = XpathImpl.getData("CmewmfJobs.xml",testcase,"servername");
     String userName = XpathImpl.getData("CmewmfJobs.xml",testcase,"username");
	 //encoded passowrd is retrieved and decoded
     String encodePass  = XpathImpl.getData("CmewmfJobs.xml",testcase,"password");
	 String password  = new String(Base64.decodeBase64(encodePass.getBytes()));
	 String mfjob	=	"BST.SRINIWEB.TESTS(RQ15T001)";	
	 FTPClient ftp = new FTPClient(); 
     FileUpload fu = new FileUpload();
	 //Connect to the server 
	 try { 
          ftp.connect (serverName) ; 
          String replyText =ftp.getReplyString()  ; 
          System.out.println(replyText) ;  
	     } 
     catch (Exception  e)  {
               e.printStackTrace () ; 
     } 
     //Login to the server 
     try { 
               ftp.login (userName, password) ; 
               String replyText = ftp.getReplyString() ; 
               System.out.println(replyText); 
           
				   
     } catch (Exception e) { 
               e.printStackTrace(); 
     } 
     //Tell server that the file will have JCL records 
     try { 
               ftp.site ("filetype=jes") ; 
               String replyText = ftp.getReplyString() ; 
               System.out.println(replyText) ;
        } 
     catch 	(Exception e) { 
               e.printStackTrace() ; 
     } 
     //Submit the job from the text file and retrieve the job output to local machine
     try { 
		 	   String job = "cmewjobs/ftp.jcl";
			   String inputLine = null;
			   StringBuffer sf = new StringBuffer();
			   BufferedReader in = new BufferedReader(new FileReader(job));
			   while((inputLine = in.readLine()) != null)
			   {
				   sf.append(inputLine);
				   sf.append("\n");
			   }
			   in.close();
			  
			   sf.append(serverName);
			   sf.append("\n");
			   sf.append(userName);
			   sf.append("\n");
			   sf.append(password);
			   sf.append("\n");
			   sf.append("quote site filetype=jes");
			   sf.append("\n");
			   sf.append("PUT "+"'"+ mfjob+"'");
			   sf.append("\n");
			   sf.append("QUIT");
			   sf.append("\n");
			   
			   BufferedWriter out = new BufferedWriter(new FileWriter("cmewjobs/ftp2.jcl"));
			   out.write(sf.toString());
			   out.close();
			   
			   FileInputStream inputStream = new FileInputStream ("cmewjobs/ftp2.jcl") ; 
               
			   ftp.storeFile (serverName,inputStream) ; 
               String replyText = ftp.getReplyString() ; 
               System.out.println(replyText) ;
			   inputStream.close();
			   
			   File f1 = new File("cmewjobs/ftp2.jcl");
			   System.out.println(f1.exists());
			   System.out.println(f1.delete());
				 
			   /*
			   //find out the Correct job submitted on mainframe
			   int jobno = replyText.indexOf("JOB");
			   jobName = replyText.substring(jobno,jobno+8);
			   System.out.println("Index of Job string is ... "+ jobno);
			   System.out.println("Job Name is   "+ jobName);
			   
			   String[] s1 = ftp.listNames();
			   for(int i=0;i<s1.length;i++)
			   {
				   System.out.println(s1[i]);
			   }
			   System.out.println("Remote system is " + ftp.getSystemName());
			   
			   //Retrieving the Job output from Mainframe
			   OutputStream output = new FileOutputStream("cmewjobs/ftpout1.txt");
               ftp.retrieveFile(jobName, output);
			   //System.out.println("ftp retr is " + ftp.retr("jobName"));
			   output.close();
				*/	  		   
     } 
     catch	(Exception e) { 
               e.printStackTrace() ; 
     } 
     //Quit the server 
     try { 
                 ftp.quit() ; 
				 System.out.println("Ftp Closed");
     } 
     catch 	(Exception e) { 
                e.printStackTrace() ; 
     }
	 
  }//end of main method 
}//end of Class

