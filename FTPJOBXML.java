/**
 * Writing job output into an XML file
 */
package mfjobftp;

import java.io.*;
import java.util.*;

import org.apache.commons.codec.binary.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.lang.*;


/**
 * @author pansr01
 *
 */
public class FTPJOBXML {

	/**
	 * @param args
	 */
	public static ArrayList execute(String testCase,String CmewmfJobs,String jobno,String jobname,String RC,String site) { 
	//public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String testcase 	= 	testCase;
		 String jobNo 		=  	jobno;
		 String jobOwner 	= 	jobname;
		 String jeslog 		= 	"joblog/jeslog.txt";
		 String serverName 	= 	XpathImpl.getData(CmewmfJobs,testcase,"servername");
	     String userName 	= 	XpathImpl.getData(CmewmfJobs,testcase,"username");
		 //encoded passowrd is retrieved and decoded
	     String encodePass  = 	XpathImpl.getData(CmewmfJobs,testcase,"password");
		 String password  	= 	new String(Base64.decodeBase64(encodePass.getBytes()));
		 //String logfile 	= 	XpathImpl.getData("CmewmfJobs.xml",testcase,"logdir") + testcase+".txt";
		 String logfile		=	"joblog/jobdir.txt"; 
		 String xmlfile 	= 	XpathImpl.getData(CmewmfJobs,testcase,"logdir") + testcase+".xml";
		 String step		=	"STEPNAME";
		 String ddname		=	"DDNAME";
		 String MaxRc		= 	"0000";
		 String spool		=	"";
		 int jesfilescount	=	0;
		
		 
		 Map idnames		=	new HashMap();//ID names from Jes Output Summary
		 Map steps			=	new HashMap();//stepnames from Jes Output Summary
		 Map ccodes 		=	new HashMap();// Return Codes from Jes Output Summary
		 Map stepnames		=	new HashMap();//Each step names 
		 Map ddnames		=	new HashMap();//Every DDnames
		 
		 ArrayList maparray	=	new ArrayList();// to return Map Array of ID, STEPNAME and DDNAME 
		 
		 FTPClient ftp		= 	new FTPClient();//ftpClient Object 
		 
		 try { 
	          
			 	//Reading the job Directory and find the no of files retrieved.
			 	spool = Utilities.readLog(logfile,"spool").trim();
				int start = spool.indexOf("spool");
				jesfilescount = Integer.parseInt(spool.substring(0,start).trim());
				System.out.println("No of Spool Files is ...  "+jesfilescount);
				
				// Retrieving the stepnames and DDnames from job log to Maps
				Utilities.getStepDDname(logfile,jesfilescount,idnames,stepnames,ddnames);
				   
				   for(int i=0;i<stepnames.size();i++)
				   {
					   System.out.print("ID is ");
					   System.out.print((String)idnames.get(new Integer(i+1)));
					   System.out.print("    ");
					   System.out.print("StepName is ");
					   System.out.print((String)stepnames.get(new Integer(i+1)));
					   System.out.print("    ");
					   System.out.print("DDname is ");
					   System.out.println((String)ddnames.get(new Integer(i+1)));
				   }
				  
			  //Connecting to Ftp Server		 
			  ftp.connect (serverName) ; 
	          ftp.login (userName, password) ; 
              //ftp.site ("filetype=jes jesjob=PANSR01B jesowner=* jesstatus=ALL") ;
			  ftp.site (site);
			   //Retrieving the Job output from Mainframe
			   OutputStream output1 = new FileOutputStream(logfile);
			   ftp.retrieveFile(jobNo+".1", output1);
			   output1.close();
			   
			  //Retrieving the Steps and CCodes to Map		   
			  stepSummmary(logfile,jobNo,steps,ccodes);
			  MaxRc = RC;					   			  
			  BufferedWriter out = new BufferedWriter(new FileWriter(xmlfile));
			  //out.write("<?xml version="+"'1.0'"+ " encoding="+"'ISO-8859-1'"+"?>");
				out.write("<Joblog>");
				out.write("\n");
				//out.write("<JOB JOBNAME="+"'PANSR01B'"+" JOBNUM="+"'"+jobNo+"'"+" MaxRC="+"'"+MaxRc+"'"+">");
				out.write("<JOB>");
				out.write("<JOBNAME>");out.write("PANSR01B");out.write("</JOBNAME>");
				out.write("<JOBNUM>");out.write(jobNo);out.write("</JOBNUM>");
				out.write("<MaxRC>");out.write(MaxRc);out.write("</MaxRC>");
				out.write("\n");
				
				//creating Xml Header
				createHeader(steps,ccodes,out);
				
			  boolean first 	= true;
			  String  prevstr 	= "";
			  boolean end  		= false;
			  for(int i=0;i<stepnames.size();i++)
			   {
				  String idName = "";
				  String stepName = "";
				  String ddName   = "";
				  String idname   = "";
				  String step1	 = "";
				  String ccode1	 = "0000";
				 
				  //Retrieving the Job output from Mainframe
				   OutputStream output = new FileOutputStream(jeslog);
				   ftp.retrieveFile(jobNo+"."+(i+1), output);
				   output.close();
				   idName 	= (String)idnames.get(new Integer(i+1));
				   stepName = (String)stepnames.get(new Integer(i+1));
				   ddName   = (String)ddnames.get(new Integer(i+1));
				   step1 	= (String)steps.get(new Integer(i+1));
				   ccode1	= (String)ccodes.get(new Integer(i+1));
				   idname  = stepName +"."+ddName;
				   
				   for(int j=0;j<steps.size();j++)
				   {
					   step1 	= (String)steps.get(new Integer(j+1));
					   if(stepName.equalsIgnoreCase(step1))
					      ccode1	= (String)ccodes.get(new Integer(j+1));
				   }
				   //Creating Step/DD output
				   if(!(prevstr.equalsIgnoreCase(stepName)))
				   {
					   end = true;
					   if(!(first) && (end))
						{	
							//System.out.println("Condition testing "+ first+" and "+ end);
							out.write("</STEP>");
						    out.write("\n");
						}
				   if(!(ccode1.equalsIgnoreCase("FLUSH")))
				   {	
					   //out.write("<STEP Stepname="+"'"+stepName + "'"+" RC="+"'"+"_"+ccode1+"'"+">");
					   out.write("<STEP>");out.write("<Stepname>");out.write(stepName);out.write("</Stepname>");
					   out.write("<RC>");out.write("_"+ccode1);out.write("</RC>");
				   }
					else
					{	
						//out.write("<STEP Stepname="+"'"+stepName + "'"+" RC="+"'"+ccode1+"'"+">");
						out.write("<STEP>");out.write("<Stepname>");out.write(stepName);out.write("</Stepname>");
					    out.write("<RC>");out.write("_"+ccode1);out.write("</RC>");
					}
				   }//
				   prevstr = stepName;
				   createXml(out,jeslog,ddName,idName);
				   first = 	false;
				   end	 =	false;
			   }// end of for loop
			  out.write("</STEP>");
			  out.write("\n");
			  out.write("</JOB>");
			  out.write("\n");
			  out.write("</Joblog>");
			  out.close();
			  ftp.quit();
			  System.out.println("XML File Creation Process Completed");
			  
			  maparray.add(0,idnames);
			  maparray.add(1,stepnames);
			  maparray.add(2,ddnames);
			  
			  //System.out.println(" Parsing Started");
				//String s1 = XpathImpl.getData(xmlfile,"ADDPKELM.C1MSGS1","Output");
			//	System.out.println(s1);
			  		  			   
		   } // end of try
		 catch 	(Exception e) { 
	        e.printStackTrace(); 
		 }
	return maparray;
	
	}//end of execute method
	
	//Create a Log file using the Jobno
	public static void createHeader(Map steps,Map ccodes,BufferedWriter out){
		
		String step 	= null;
		String ccode   	= null;
		try{
			out.write("<JOBSUMMARY Stepname="+"'Summary'"+">");
			for(int i=0;i<steps.size();i++)
			{
				step   =  (String)steps.get(new Integer(i+1));
				ccode  =  (String)ccodes.get(new Integer(i+1));
				if(!(ccode.equalsIgnoreCase("FLUSH")))
				{  
					//out.write("<STEPSUMMARY RC="+"'"+"_"+ccode+"'"+" Stepname="+"'"+step+"'"+"/>");
					out.write("<STEPSUMMARY>");out.write("<RC>");out.write("_"+ccode);out.write("</RC>");
					out.write("<Stepname>");out.write(step);out.write("</Stepname>");
					out.write("</STEPSUMMARY>");
				}
				else
				{	
					//out.write("<STEPSUMMARY RC="+"'"+ccode+"'"+" Stepname="+"'"+step+"'"+"/>");
					out.write("<STEPSUMMARY>");out.write("<RC>");out.write(ccode);out.write("</RC>");
					out.write("<Stepname>");out.write(step);out.write("</Stepname>");
					out.write("</STEPSUMMARY>");
				}
				out.write("\n");
				
			}
			out.write("</JOBSUMMARY>");
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
	
	//Create a Log file using the Jobno
	public static void createXml(BufferedWriter out,String infile,String ddname,String id){
		
		try{
			
			//creating a BufferWriter to create and write into the file locally 
			BufferedReader in = new BufferedReader(new FileReader(infile));
			//out.write("<DD DDname="+"'"+ddname+"'"+" id="+"'"+id+"'"+">");
			
			out.write("<DD id="+"'"+id+"'"+">");
			out.write("<DDname>");
			out.write(ddname);
			out.write("</DDname>");
			out.write("\n");
			out.write("<Output>");
			out.write("\n");
			System.out.println("Processing DDname " + ddname);
			String inputLine = "";
			
			while ((inputLine = in.readLine()) != null)
			{
				inputLine = inputLine.trim();
				if(inputLine.contains("&"))
				{
					inputLine = inputLine.replaceAll("&","&amp;");
				}
				if(inputLine.length() > 1)
				{
					out.write(inputLine);
					out.write("\n");
				}
						
			}//
			out.write("</Output>");
			out.write("\n");
			out.write("</DD>");
			out.write("\n");
			in.close();
						
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
	}//end of createLog
	
	//Create a Log file using the Jobno
	public static void stepSummmary(String infile,String jobno,Map steps,Map ccodes ){
		int stepnameind 	= 0;
		int stepind 		= 0;
		int ccodeind		= 0;
		int jobnameind 		= 0;
		int counter 		= 1;
		String stepname 	= "STEPNAME STEP";
		String step			= "STEP   PGM=";
		String ccode		= "CCODE";
		BufferedReader in;
		String inputLine 	= null;
		try{
			// first Iteration
			in = new BufferedReader(new FileReader(infile));
			while (!(inputLine = in.readLine()).contains("JOB ENDED. TOTAL EST-COST"))
			{
				if(inputLine.contains(stepname))
				{
					
					jobnameind  = inputLine.indexOf(jobno);
					stepnameind = inputLine.indexOf(stepname);
					stepind     = inputLine.indexOf(step);
					ccodeind    = inputLine.indexOf(ccode);
					System.out.println(jobnameind +"  " +stepnameind);
				}
			}//
			in.close();
			
			//Second Iteration
			in = new BufferedReader(new FileReader(infile));
			while (!(inputLine = in.readLine()).contains("JOB ENDED. TOTAL EST-COST"))
			{
				if(inputLine.contains(jobno))
				{
					String temp = inputLine.substring(stepind,stepind+4).trim();
					if(temp.equalsIgnoreCase(""+counter))
					{
						//System.out.println(counter);
						steps.put(new Integer(counter),inputLine.substring(stepnameind,stepnameind+8).trim());
						ccodes.put(new Integer(counter),inputLine.substring(ccodeind,ccodeind+5).trim());
						counter = counter + 1;
					}
				}
			}//
			in.close();
			
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
	}//
}//end of class

