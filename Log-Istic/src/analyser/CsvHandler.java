package analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.os.Environment;
import android.util.Log;
import entity.Http;

public class CsvHandler 
{
	/**
	 * generates csv file for trained model takes in headerlist and contentlist for generation
	 * @param fileName
	 * @param headerList
	 * @param contentList
	 * @param fragmentSelected
	 */
	public void generateCsvFile(String fileName, ArrayList<String> headerList, ArrayList<Object> contentList, int fragmentSelected)
	{
		try
		{
			boolean check = false;
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File (sdCard.getAbsolutePath() + "/Logistic/Trained_Models/");
			File file = new File (dir,fileName+".csv");
			if(!file.exists())
			{
				check = true;
			}
			
			FileOutputStream fOut = new FileOutputStream(file, true);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			
			//if there are no existing file, create CSV with headers
			if(check)
			{
				for(String header: headerList)
			    {
					myOutWriter.append(header);
					myOutWriter.append(",");
			    }
				myOutWriter.append("Malicious Class");
				myOutWriter.append('\n');
			}
			
			//appends content in contentList to csv
			if(fragmentSelected == 0)
		    {
			    for(Object o: contentList)
			    {
			    	Http h = (Http)o;
			    	HashMap<String, Integer> specialAttributes = h.getSpecialAttribute();
			    	String[] tempDate = h.getDateTime().split(",");
			    	if(tempDate.length>1)
			    		myOutWriter.append(tempDate[0]+tempDate[1]);
			    	else
			    		myOutWriter.append("");
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getPid());
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getSource());
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getDest());
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getUserAgent());
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getContentType());
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getContentLength());
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getContentEncoding());
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getAcceptEncoding());
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getMethodType());
			    	myOutWriter.append(",");
			    	myOutWriter.append(h.getParamSize());
			    	myOutWriter.append(",");
			    	myOutWriter.append(Integer.toString(specialAttributes.get("hasIMEI")));
			    	myOutWriter.append(",");	
			    	myOutWriter.append(Integer.toString(specialAttributes.get("hasIMSI")));
			    	myOutWriter.append(",");	
			    	myOutWriter.append(Integer.toString(specialAttributes.get("hasBrowserHistory")));
			    	myOutWriter.append(",");	
			    	myOutWriter.append(Integer.toString(specialAttributes.get("hasPhoneNumber")));
			    	myOutWriter.append(",");	
			    	myOutWriter.append(Integer.toString(specialAttributes.get("hasEmail")));
			    	myOutWriter.append(",");	
			    	myOutWriter.append(h.getMaliciousClass());
			    	myOutWriter.append('\n');			    	
			    }
		    }
			myOutWriter.flush();
			myOutWriter.close();
			fOut.flush();
			fOut.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
    }
	/**
	 * read CSV file and returns a arraylist of object entity
	 * @param fragmentSelected
	 * @param fileInput
	 * @return
	 */
    public ArrayList<Object> fileRead(int fragmentSelected, String fileInput) 
    {
    	ArrayList<Object> arrObj = new ArrayList<Object>();
        try 
        {
        	String encoding = "UTF-8";
			
			String filePath = Environment.getExternalStorageDirectory().getPath()+"/Logistic/Trained_Models/"+ fileInput;
			
			InputStreamReader is = new InputStreamReader(new FileInputStream(filePath), encoding);
			BufferedReader br = new BufferedReader(is);
                   
			String sCurrentLine = br.readLine();
            String[] hl = sCurrentLine.split(",");
            //Use for creating the header list
            ArrayList<String> headerList = new ArrayList<String>(Arrays.asList(hl));
            if(fragmentSelected == 0)
            {
            	//read in per line and set it into Http Entity and to an Arraylist<Object>
	            while ((sCurrentLine = br.readLine()) != null) 
	            {
	            	HashMap<String,Integer> sAttributes = new HashMap<String,Integer>();
	                String[] ar = sCurrentLine.split(",");
	                Http h = new Http();
	                h.setDateTime(ar[0]);
	                h.setPid(ar[1]);
	                h.setSource(ar[2]);
	                h.setDest(ar[3]);
	                h.setUserAgent(ar[4]);
	                h.setContentType(ar[5]);
	                h.setContentLength(Integer.parseInt(ar[6]));
	                h.setContentEncoding(ar[7]);
	                h.setAcceptEncoding(ar[8]);
	                h.setMethodType(ar[9]);
	                h.setParamSize(Integer.parseInt(ar[10]));
	                sAttributes.put("hasIMEI", Integer.parseInt(ar[11]));
	                sAttributes.put("hasIMSI", Integer.parseInt(ar[12]));
	                sAttributes.put("hasBrowserHistory",Integer.parseInt(ar[13]));
	                sAttributes.put("hasPhoneNumber", Integer.parseInt(ar[14]));
	                sAttributes.put("hasEmail", Integer.parseInt(ar[15]));
	                h.setSpecialAttribute(sAttributes);
	                h.setMaliciousClass(Integer.parseInt(ar[16]));
	                arrObj.add(h);
	            }
            }
            else if(fragmentSelected == 1)
            {
            	
            }
            else if(fragmentSelected == 2)
            {
            	
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return arrObj;

    }//end file read
}
