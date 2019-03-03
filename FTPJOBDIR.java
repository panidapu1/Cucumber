//Retrieving the Job outout when the job completed
package mfjobftp;

import com.enterprisedt.net.ftp.*;

import java.io.*;
import java.util.*;
import java.net.InetAddress;
import java.net.SocketException;
import org.apache.commons.codec.binary.*;
//import org.apache.commons.net.ftp.FTPClient;

/**
 * @author pansr01
 *
 */
public class FTPJOBDIR {

	/**
	 * @param args
	 */
	public static String execute(String testCase,String CmewmfJobs,String jobno,String jobname,boolean isend) { 
	//public static void main(String[] args) {
		// TODO Auto-generated method stub
		  
		 String testcase 	= 	testCase;
		 String jobNo 		=  	jobno;
		 String jobOwner 	= 	jobname;
		 
	     String serverName 	= 	XpathImpl.getData(CmewmfJobs,testcase,"servername");
	     String userName 	= 	XpathImpl.getData(CmewmfJobs,testcase,"username");
		 //encoded passowrd is retrieved and decoded
	     String encodePass  = 	XpathImpl.getData(CmewmfJobs,testcase,"password");
		 String password  	= 	new String(Base64.decodeBase64(encodePass.getBytes()));
		 
		 String logfile		=	"joblog/jobdir.txt"; 
		// String logfile 	= 	XpathImpl.getData("CmewmfJobs.xml",testcase,"logdir") + testcase+".txt";
		 String site		=	"filetype=jes jesjob="+jobname+" jesowner=* jesstatus=ALL";
		 String parsestatus = 	"JOBNAME"+ "  " + "JOBID";
		 String parseStr 	= 	jobOwner + " " + jobNo; 
		 String statusStr 	= 	"";
		 String jobstr	  	= 	"";	
		 String returnCode 	= 	"";
		 String jobstatus 	= 	"";
				 
		 FTPClient ftp 		= 	new FTPClient(); 
				 
		 //FTp Dir command process
		 try { 
			
			 ftp.setRemoteHost(serverName);
			 ftp.login (userName, password);
			 ftp.site(site);
			 long timeout = 3*60*60*1000;
			 long duration = 10*1000;
			 long counter = 0;
			 boolean jobcond = true;
			 
			 while(jobcond){
			 
			//creating a BufferWriter to create and write into the file locally 
	          
				createLog(logfile,ftp,jobNo);
				
			 //Reading the log file locally
				statusStr = Utilities.readLog(logfile,parsestatus);
				statusStr.trim();
				jobstr    = Utilities.readLog(logfile,parseStr);
			
				int statusInd = statusStr.indexOf("STATUS");
				jobstatus = jobstr.substring(statusInd,statusInd+6);
				System.out.println("Job String is  "+ jobstr);
				System.out.println("Job Status is  "+ jobstatus);
			
				//Checking the status of the job	
				if(jobstatus.contentEquals("OUTPUT")){
					System.out.println("Job Ended Succesfully");
					statusInd  = jobstr.indexOf("RC=");
					returnCode = jobstr.substring(statusInd,statusInd+7);
					System.out.println("Return Code is ...  "+returnCode);
					jobcond = false;
				}
				else
				{
					if (timeout==counter)
					{	
						System.out.println("\n");
						System.out.println("Job Waited for 3 Hours and did not Completed .......");
						returnCode = "Job Not Completed";
						jobcond = false;
					}
					else
					{
						System.out.println("Waiting for Job to complete .......");
						Thread.sleep(duration);
						counter = counter + duration;
					}// end of inner if loop
				}//end of uter if loop
			 }//end of while loop
			 
			 //Quit the server
			  ftp.quit() ; 
			/* 
			 // for Clean up of Inventory of Testcase then do not create XML file for job output
			 if(!isend)
			 		FTPJOBXML.execute(testCase,jobno,jobname,returnCode,site);	 
			*/	
		 } //end of try
		 catch (InterruptedException ie) {
			 ie.printStackTrace();
		 }//
		 catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		 }
		 catch (IOException ex){
		      ex.printStackTrace();
		 }	 
		 catch 	(Exception e) { 
	        e.printStackTrace(); 
		 }
		 
	return returnCode;
	}//end of execute method
	
	//Create a Log file using the Jobno
	public static void createLog(String logfile,FTPClient ftp,String jobno ){
		
		try{
			//creating a BufferWriter to create and write into the file locally 
			FileOutputStream fout =  new FileOutputStream(logfile); 
			//BufferedWriter myOutput = new BufferedWriter(new FileWriter(logfile)); 
			// now to the FileOutputStream into a PrintStream 
		      PrintStream myOutput = new PrintStream(fout); 

		String[] s1 = ftp.dir(jobno,true);
		 for(int i=0;i<s1.length;i++)
		 {
			 myOutput.println(s1[i]);
			 System.out.println(s1[i]);
		 }
		 myOutput.close();
		}
		catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		 }
		 catch (IOException ex){
		      ex.printStackTrace();
		 }	 
		 catch 	(Exception e) { 
	        e.printStackTrace(); 
		 }
	}//end of createLog
	
}//end of class
