/**
 * 
 * @author Bernard
 *	
 */

package logreader;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import entity.Http;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import androidutility.AndroidInfoUtil;

/**
 * Handles log files
 */
public class FileHandler 
{	
	//HTTP Declaration to store temporary value to be inserted into HTTP entity.
	private String pid; 
	private String application;
	private String userAgent;
	private String dateTime;
	private String dest;
	private String contentType;
	private int contentLength;
	private String contentEncoding;
	private String acceptEncoding;
	private String methodType;
	private int paramSize;
	private String msg;
	private int maliciousClass;
	private HashMap<String, Integer> specialAttributes = new HashMap<String, Integer>();

	//stores the fragment(tab) selected in the UI 
	private int fragmentSelected;
	
	//stores a list of entity objects
	private ArrayList<Object> oList = new ArrayList<Object>();
	
	//used to store list of log file names to be deleted
	private ArrayList<File> logList = new ArrayList<File>();
	
	
	/**
	 * Reads log file according to the type selected.
	 * @param type
	 * @param ac
	 * @return
	 */
	public ArrayList<Object> readFile(String type)
	{
		initialiseAttributes();
		
		String fileName = null;
		String directoryPath = null;
		int choice = 0;
		
		// gets http log files from sdcard
		if(type == "Http")
		{
			fragmentSelected = 0;
			fileName = "Http";
			directoryPath = Environment.getExternalStorageDirectory().getPath()+"/Logistic/Http/";
			choice = 1;
		}
		else if(type == "SMS")
		{
			fragmentSelected = 1;
			fileName = "SMS";
			directoryPath = Environment.getExternalStorageDirectory().getPath()+"/Logistic/SMS/";
			choice = 2;
		}
		else if(type == "Phone")
		{
			fragmentSelected = 2;
			fileName = "Phone";
			directoryPath = Environment.getExternalStorageDirectory().getPath()+"/Logistic/Phone/";
			choice = 3;
		}
		
		//Executing read sequence
		try
		{
			String encoding = "UTF-8";//8859-1
			String filePath;
			// -f flag is important, because this way ls does not sort it output,
			// find log files 
			String[] extraLogFilesparams = { "bash", "-c",
			    "find " + directoryPath + " -name \""+fileName+".log.*.*\" | wc -l" };
			Process process = Runtime.getRuntime().exec(extraLogFilesparams);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String extraFileCount = reader.readLine().trim();
			String[] totalFilesParam = { "bash", "-c",
				    "find " + directoryPath + " -name \""+fileName+".log.*\" | wc -l" };
			process = Runtime.getRuntime().exec(totalFilesParam);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String totalFileCount = reader.readLine().trim();
			reader.close();
	
			int extraLogs = Integer.parseInt(totalFileCount)-Integer.parseInt(extraFileCount);

			
			//-------------------Multiple log files due to race condition on file-----------------
			//read all log files that ends with ----.log.*
			for(int j = 1; j <= extraLogs; j++)
			{	
				filePath = directoryPath + fileName+".log."+j;
				//read log file
				readSelection(choice, encoding, filePath);
				//add log file path into list for deletion
				populateLogFile(filePath);
			}
			//read log file that ends with ----.log
			filePath = directoryPath + fileName+".log";
			readSelection(choice, encoding, filePath);
			populateLogFile(filePath);
			//read log file that ends with ----2.log
			filePath = directoryPath + fileName+"2.log";
			readSelection(choice, encoding, filePath);
			populateLogFile(filePath);
		}
		catch(Exception ex)
		{
			Log.e("ERROR", ex.getMessage());
		}
		
		//conduct sort on object list
		Collections.sort(oList, new MyObjectComp());
		return oList;
	}
	
	/**
	 * This inner class is used to create a comparator object for sorting to sort all log records according to its date
	 * @author Bernard
	 *
	 */
	class MyObjectComp implements Comparator<Object>
	{		 
		@Override
		public int compare(Object o1, Object o2) 
		{
			if(fragmentSelected == 0)				
				return ((Http) o1).getDateTime().compareTo(((Http) o2).getDateTime());   
			// else other fragments
			return 0;
		}
	}
	
	/**
	 * populate extra logs into a list for removal
	 * @param filePath
	 */
	private void populateLogFile(String filePath)
	{
		File logFile = new File (filePath);
		logList.add(logFile);
	}
	/**
	 * remove log files
	 */
	public void removeLogFiles()
	{
		for(File f : logList)
		{	
			f.delete();
		}
	}
	
	/**
	 * Makes selection of which type of file to read
	 * takes in file path and choice selection
	 * @param choice
	 * @param encoding
	 * @param filePath
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readSelection(int choice, String encoding, String filePath) throws UnsupportedEncodingException, FileNotFoundException, IOException
	{
		InputStreamReader is = new InputStreamReader(new FileInputStream(filePath), encoding);
		BufferedReader br = new BufferedReader(is);
		
		//conducting read according to fragmentselected(tab)
		switch(choice)
		{
			case 1:
				readHttp(br);
				break;
			case 2:
				readSMS(br);
				break;
			case 3:
				readPhone(br);
				break;
			default:
				break;		
				
		}
		br.close();
	}
	/**
	 * Break down of HTTP logs according to headers to pick out relevant information
	 * PID, packagename, request header, request body, date time
	 * @param BufferedReader br
	 * @throws IOException
	 */
	private void readHttp(BufferedReader br) throws IOException
	{
		String line;		
		
		boolean endOfRequest = false;
		boolean dateTimeCheck = false;

		//reads in line by line
		while( (line = br.readLine()) !=null)
		{
			String tempLine;
			//each request starts with "INFO" or "PID"
			if(line.contains("INFO:") || line.contains("PID ") )
			{
				/***********if the next line that appears with "INFO" or "PID" means the previous HTTP log is completed*********
				If the flag is true, it will add all the attributes of the http record into the HTTP entity and the object list*/
				checkEndOfRequest(endOfRequest);
				
				//start of http request, may start with INFO
				if(line.contains("INFO"))
				{
					int start = line.indexOf(":");					
					tempLine = line.substring(start + 1); 
				}
				else
				{
					tempLine = line;
				}
				
				//Get PID and package Name
				if(tempLine.contains(","))
				{
					//pid:####, packagename:@@@@@
					String[] pidAndPkgname = tempLine.split(",");
					String[] pidString = pidAndPkgname[0].split(":");
					pid = pidString[1];
					if(pidAndPkgname[1].isEmpty())
					{
						String[] pkgNameString = pidAndPkgname[1].split(":");
						application = pkgNameString[1];
					}
				}
				//does not contain package name
				else
				{
					String[] pidString = tempLine.split(":");
					pid = pidString[1];
				}
				//set boolean to true, the next time if a log request contains INFO or PID, a HTTP entity will be initialised with the accumulated attribute values
				//this boolean is used at checkEndOfRequest(endOfRequest); at the start of this if case
				endOfRequest = true;
			}
			//if http request contains packageName heading
			else if(line.contains("PackageName"))
			{			
				String[] appString = line.split(":");
				if(appString.length == 2)
					application = appString[1];
			}
			//if http request contains Request Headers heading
			else if(line.contains("Request Headers"))
			{
				int start = line.indexOf(":");
				String temp = line.substring(start + 1); 
				
				breakRequestHeader(temp);
			}
			//if http request contains Request Body (/?include_headers heading
			else if(line.contains("Request Body (/?include_headers"))
			{
				int start = line.indexOf(":");
				
				//double encoding is formed when transmitted, decoding is required
				String tempBodyMsg = URLDecoder.decode(line.substring(start + 1), "UTF-8");
				tempBodyMsg = URLDecoder.decode(tempBodyMsg, "UTF-8");
				//replace comma with ampersand, replace newline with ""
				msg = tempBodyMsg.replaceAll(",", "&").replaceAll("\\n", "");
				
				//conducts breaking down of request body message in this method
				breakRequestBody(tempBodyMsg);
			}
			else if(line.contains("Request Body"))
			{
				int start = line.indexOf(":");
				
				String tempBodyMsg = line.substring(start + 1); 
				
				tempBodyMsg = URLDecoder.decode(tempBodyMsg, "UTF-8");
				msg = tempBodyMsg.replaceAll(",", "&");
				breakRequestBody(msg);
				
				
			}
			
			//checks for timing PM
			else if(line.contains(" PM ") && !dateTimeCheck)
			{
				int end = line.indexOf("PM")+2;
				dateTime = line.substring(0, end);
				dateTimeCheck = true;
			}
			
			//checks for timing AM
			else if(line.contains(" AM ") && !dateTimeCheck)
			{
				int end = line.indexOf("AM")+2;
				dateTime = line.substring(0, end);
				dateTimeCheck = true;
			}
		}
		//after looping, conducts one more checkendrequest to key in final http request
		checkEndOfRequest(endOfRequest);
	}

	private void readSMS(BufferedReader br) throws IOException
	{
		//do break down of logs here
	}
	private void readPhone(BufferedReader br) throws IOException
	{
		//do break down of logs here
	}
	/**
	 * Used in readHttp()
	 * Breaks down request header
	 * Can add in new items if required
	 * @param temp
	 */
	private void breakRequestHeader(String headerMsg) 
	{
		//splits the header by the ampersand sign
		String[] headerMsgArray = headerMsg.split("&");
		
		//for each header in request header
		for(int i = 0; i< headerMsgArray.length; i++ )
		{
			String section = headerMsgArray[i];
			//splits each by key and value (Section contains Key=Value) 
			String[] tempArray2 = section.split("=");

			//if key equals to Host
			if(tempArray2[0].contentEquals("Host"))
			{
				//this is the destination address
				 dest = tempArray2[1]; 
			}
			//if key equals to User-Agent
			if(tempArray2[0].contentEquals("User-Agent"))
			{
				int length = tempArray2.length;
				if(length>2)
				{
					String uA = "";
					//concatenate the user-agent value
					for(int j  = 1; j<length; j++)
					{
						uA += tempArray2[j];
					}
					//remove all spacing, remove all {} signs
					userAgent = uA.replaceAll("\\s+", "").replaceAll("[{},]+", "");
				}
				else
				{
					userAgent = tempArray2[1].replaceAll("\\s+", "").replaceAll("[{},]+", "");
				}
			}
			//if key equals to Content-Type
			if(tempArray2[0].contentEquals("Content-Type"))
			{
				int end = tempArray2[1].indexOf(";");
				if(end != -1)
					//remove trailing ; sign and replaces all , sign with ;
					contentType = tempArray2[1].substring(0, end).replaceAll(",", ";");
				else
					contentType = tempArray2[1].replaceAll(",", ";"); 
			}
			//if key equals to Content-Encoding
			if(tempArray2[0].contentEquals("Content-Encoding"))
			{	
				contentEncoding = tempArray2[1].replaceAll(",", ";");
			}
			//if key equals to Accept-Encoding
			if(tempArray2[0].contentEquals("Accept-Encoding"))
			{	
				acceptEncoding = tempArray2[1].replaceAll(",", ";");
			}
			//if key equals to Content-Length
			if(tempArray2[0].contentEquals("Content-Length"))
			{
				contentLength = Integer.parseInt(tempArray2[1]); 
			}
		}		
	}
	/**
	 * Used in readHttp()
	 * Breaks down request body
	 * Can add in new items if required
	 * @param msg
	 */
	private void breakRequestBody(String bodyMsg)
	{
		int start = bodyMsg.indexOf(":");
		//request body message
		bodyMsg = bodyMsg.substring(start+1);
		//used for first occurence of the method type
		boolean mTypeCheck = false;
		//splits request body by ampersand sign
		String[] stringParams = bodyMsg.split("&");
		paramSize = stringParams.length;
		
		//request body is not empty, more than 1 parameter
		if(paramSize != 1)
		{			
			for(int i = 0; i < paramSize; i++)
			{			
				//splits each by key and value (Section contains Key=Value) 
				String[] param = stringParams[i].split("=");
				if(param.length < 2)
					param = stringParams[i].split(":");
				
				//get Get or Post method
				if((param[0].contains("method")||param[0].contains("\"method\""))&& !mTypeCheck)
				{
					if(param.length != 1)
					{
						if(param[1].contains("get")||param[1].contains("GET"))
							methodType = "GET";
						else if(param[1].contains("post")||param[1].contains("POST"))
							methodType = "POST";
						else
							methodType = "";
						mTypeCheck = true;
					}
				}
				//if any of the below is identified, it is classifed as malicious hence  maliciousClass = 1; 
				//checks if contain IMEI, uses AndroidInfoUtil.class
				if(param[0].contains("IMEI") || AndroidInfoUtil.isIMEI(param[1]))
				{
					maliciousClass = 1; 
					specialAttributes.put("hasMEI", 1);
				}
				//checks if contain IMSI, uses AndroidInfoUtil.class
				if( AndroidInfoUtil.isIMSI(param[1]))
				{
					maliciousClass = 1; 
					specialAttributes.put("hasIMSI", 1);
				}
				//checks if contain brwoser history, uses AndroidInfoUtil.class
				if(AndroidInfoUtil.isBrowserHistory(param[1]))
				{
					maliciousClass = 1; 
					specialAttributes.put("hasBrowserHistory", 1);
				}				
				//checks if contain phone number, uses AndroidInfoUtil.class
				if(AndroidInfoUtil.isPhoneNumber(param[1]))
				{
					maliciousClass = 1; 
					specialAttributes.put("hasPhoneNumber", 1);
				}
				//checks if contain email, uses AndroidInfoUtil.class
				if(AndroidInfoUtil.isEmail(param[1]))
				{
					maliciousClass = 1; 
					specialAttributes.put("hasEmail", 1);
				}
			}
		}
	}
	/**
	 * adds completed object into arraylist of object
	 * This object list will be used for processing
	 * @param endOfRequest
	 */
	private void checkEndOfRequest(boolean endOfRequest)
	{
		if(endOfRequest)
		{
			if(fragmentSelected == 0)
			{
				//set flag back to false for the next http log record
				endOfRequest = false;
				
				//initialise entity
				Http httpEntity = new Http(pid, application, dateTime, dest, contentType, contentLength, contentEncoding, acceptEncoding, methodType,
						paramSize,  msg, userAgent, specialAttributes);
				
				httpEntity.setMaliciousClass(maliciousClass);
				
				//add to object list
				oList.add(httpEntity);

				//reinitialise the attributes
				initialiseAttributes();
			}
			else if(fragmentSelected == 1)
			{
				//for sms
			}
			else if(fragmentSelected == 1)
			{
				//for phone
			}
		}
	}
	
	
	/**
	 * intialise attributes for different fragments
	 */	
	private void initialiseAttributes() 
	{
		if(fragmentSelected == 0)
		{
			pid = ""; 
			application = "";
			userAgent = "";
			dateTime = "";
			dest = "";
			contentType = "";
			contentLength = 0;
			contentEncoding = "";
			acceptEncoding = "";
			methodType = "";
			paramSize = 0;
			msg = "";
			maliciousClass = 0;
			populateHttpSpecialAttributeMap();
		}
		else if(fragmentSelected == 1)
		{
			//for sms
		}
		else if(fragmentSelected == 1)
		{
			//for phone
		}
		
	}
	
	/**
	 * initialise special attributes for http request into a hashmap
	 */
	private void populateHttpSpecialAttributeMap()
	{
		specialAttributes.put("hasIMEI", 0);
		specialAttributes.put("hasIMSI", 0);
		specialAttributes.put("hasBrowserHistory", 0);
		specialAttributes.put("hasPhoneNumber", 0);
		specialAttributes.put("hasEmail", 0);
	}
}
