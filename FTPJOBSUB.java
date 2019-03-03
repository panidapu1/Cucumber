//************************************************************************
//Ftptest - Used to send the Mainframe Job from PC to Mainframe and Submit the 
//job on Mainframe. Upon Completion of the Job, Get the Job Output to the 	 	
//Local Machine.
//
//***********************************************************************
package mfjobftp;
/**
 * @author pansr01
 *
 */
import org.apache.commons.net.ftp.*; 
import org.apache.commons.net.*;
import java.io.*;
import java.util.*;
import java.net.InetAddress;
import java.net.SocketException;
import org.apache.commons.codec.binary.*;

public class FTPJOBSUB {

	/**
	 * @param args
	 */
	//public static void main (String[] args)  { 
	public static String execute(String testcase,String CmewmfJobs,boolean isEnd,String stepname,String ddname){	 
		 //String testcase 	= 	"rq15t001";
		 String jobNo 		= 	"";
		 String jobName 	=	"";
		 String replyText	=	"";
		 //String CmewmfJobs	=	"";
		 String jobfile 	= 	XpathImpl.getData(CmewmfJobs,testcase,"jobdir") + testcase+".jcl";
		 String serverName 	= 	XpathImpl.getData(CmewmfJobs,testcase,"servername");
	     String userName 	= 	XpathImpl.getData(CmewmfJobs,testcase,"username");
		 //encoded passowrd is retrieved and decoded
	     String encodePass  = 	XpathImpl.getData(CmewmfJobs,testcase,"password");
		 String password  	= 	new String(Base64.decodeBase64(encodePass.getBytes()));
		 String site		=	"filetype=jes jesjob="+jobName+" jesowner=* jesstatus=ALL";
		 String ReturnCode  = 	"";
		 String expectedResult	=	"";
		 //String stepname	=	"RUNCSV"; //Required Stepname to look for
		 //String ddname		=	"APIEXTR"; //Required DDname to look for
		 int jobind			=	0;
		 //boolean isEnd		=	false;
		 ArrayList maparray	=	new ArrayList();
		 FTPClient 	ftp 	= 	new FTPClient(); 
	     
		 //Connect to the server 
		 try { 
	          ftp.connect (serverName) ; 
	          System.out.println(ftp.getReplyString()) ;  
		     } 
	     catch (Exception  e)  {
	               e.printStackTrace () ; 
	     } 
	     //Login to the server 
	     try { 
	               ftp.login (userName, password) ; 
				   System.out.println(ftp.getReplyString()) ; 
					   
	     } catch (Exception e) { 
	               e.printStackTrace(); 
	     } 
	     //Tell server that the file will have JCL records 
	     try { 
	               ftp.site ("filetype=jes") ; 
				   System.out.println(ftp.getReplyString()) ;	   
	             
	     } 
	     catch 	(Exception e) { 
	               e.printStackTrace() ; 
	     } 
	     //Submit the job from the text file and retrieve the job output to local machine
	     try { 
			 		//jobname is retrieved
			 		jobName = readFile(jobfile);
					// updating the SETRUN step
					updateFile(jobfile,testcase,CmewmfJobs,isEnd);
					//sending the JCL from PC to Mainframe
					FileInputStream inputStream = new FileInputStream (jobfile) ;
				    ftp.storeFile(serverName,inputStream); 
				    replyText = ftp.getReplyString();
	                System.out.println(ftp.getReplyString());
				    inputStream.close();
				   //find out the Correct job submitted on mainframe
				    jobind = replyText.indexOf("JOB");
				    jobNo = replyText.substring(jobind,jobind+8);
				    System.out.println("Index of Job string is ... "+ jobind);
				    System.out.println("Job Number is   "+ jobNo);
				   				    			  		   
	     } 
	     catch	(Exception e) { 
	               e.printStackTrace() ; 
	     } 
	     //Quit the server 
	     try { 
	                 ftp.quit() ; 
					 // retrieving Job output when the job execution completed
					 ReturnCode = FTPJOBDIR.execute(testcase,CmewmfJobs,jobNo,jobName,isEnd);
					
					 //for Clean up of Inventory of Testcase then do not create XML file for job output
					 if(!isEnd)
					 {	 
						 maparray =  FTPJOBXML.execute(testcase,CmewmfJobs,jobNo,jobName,ReturnCode,site);
					 }
					 //retrieve the Expected result
					 expectedResult = PARSECSV.getPackageNames(testcase,CmewmfJobs,maparray,stepname,ddname);
					// System.out.println("Expected Result is  : "+expectedResult);
	     } 
	     catch 	(Exception e) { 
	                e.printStackTrace() ; 
	     }
		 //return ReturnCode;
		 return expectedResult;
	  }//end of main method 
	
	//	Reading the Jcl file for parsing a string
	public static String readFile(String jobfile ){
		String inputline = "";
		String jobname = "";
		int start = 2;
		try {
		 
			BufferedReader inBuf = new BufferedReader(new FileReader(jobfile)); 
        	inputline = inBuf.readLine();
			jobname = inputline.substring(start,start+8);
			inBuf.close();
			System.out.println("Inputline is  "+ inputline);
			System.out.println("Job Name is  "+ jobname);
						
		}//end of try
		catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		 }
		 catch (IOException ex){
		      ex.printStackTrace();
		 }	 
		 catch 	(Exception e) { 
	        e.printStackTrace(); 
		 }
	 return jobname;
	}//end of readFile
	
	//Update the Jcl file for parsing a string
	public static void updateFile(String jobfile, String testcase,String CmewmfJobs,boolean isend ){
		String inputline = "";
		String setdel	=	"//SETRUN";
		int start = 0;
		String filedir = XpathImpl.getData(CmewmfJobs,testcase,"jobdir");
		String tempfile = filedir+"temp.jcl";
		try {
		 
			BufferedReader inBuf = new BufferedReader(new FileReader(jobfile)); 
			BufferedWriter out = new BufferedWriter(new FileWriter(tempfile));
			
			while((inputline = inBuf.readLine()) != null){
							
			if(inputline.contains(setdel))
			{
				if(isend)
				{
					inputline = inputline.replaceAll("0","1");
					inputline = inputline.replaceAll("2","1");
				}
				else
				{
					inputline = inputline.replaceAll("1","0");
					inputline = inputline.replaceAll("2","0");
				}
			}//end of if 
			out.write(inputline);
			out.write("\n");
			
			}// end of if loop
			// close files
			inBuf.close();
			out.close();
			
			 File f1 = new File(jobfile);
			 System.out.println("is Input job file "+ jobfile + " exits : " +f1.exists());
			 System.out.println("Before Modification of jcl file is Deleted : "+f1.delete());

			 File f2 = new File(tempfile);
			 System.out.println("Temporary file Created, Modified and Exist : "+f2.exists());
			 //System.out.println("Temp file renamed to  "+ f2.renameTo(new File(filedir+"test.jcl")));
			 System.out.println("Temp file renamed to  "+ testcase+".jcl"+"  is : " + f2.renameTo(new File(filedir+testcase+".jcl")));
				 
		}//end of try
		catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		 }
		 catch (IOException ex){
		      ex.printStackTrace();
		 }	 
		 catch 	(Exception e) { 
	        e.printStackTrace(); 
		 }
	}//end of updateFile
		
}// end of Class
