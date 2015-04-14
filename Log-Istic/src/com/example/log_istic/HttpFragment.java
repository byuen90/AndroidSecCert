package com.example.log_istic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import logreader.FileHandler;
import entity.Http;
import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HttpFragment extends Fragment 
{
	private final ArrayList<String> headerList = new ArrayList<String>();
	private ArrayList l;
	private FileHandler fhdlr;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		initialiseHeaderName();
		
		View rootView = inflater.inflate(R.layout.http_layout, container, false);
				
		rootView =  update(rootView);
        return rootView;
	}
	//-----------------------------------------private method---------------------------------------------------
	/**
	 * Display progress bar while executing file reading for the HTTP log
	 * Displays results of http log onto table in UI
	 * @param rootView
	 * @return
	 */
	private View update(final View rootView)
	{
		class Task extends AsyncTask<ArrayList<Object>, Void, ArrayList<Object>>
        {
			ProgressDialog progress;
			@Override
			protected void onPreExecute() {
				progress = new ProgressDialog(getActivity());
				progress.setMessage("Loading");
				progress.setIndeterminate(true);
				progress.setCancelable(false);
				progress.show();
		    }
			//loading content
			protected ArrayList<Object> doInBackground(ArrayList<Object>... rootView) 
			{
				// stores content into an arraylist and used for processing in OnPostExecute 
				fhdlr = new FileHandler();
        		l = fhdlr.readFile("Http");
        		return l;
			} 
			//displaying content on UI
			protected void onPostExecute(ArrayList<Object> results) 
			{
        		progress.dismiss();

    			TableLayout httpTable = (TableLayout) rootView.getRootView().findViewById(R.id.tableLayoutHttp);
    			initialiseHeadersOnTable(httpTable);
    			
        		int items = results.size();
        		int startOfItems = 0;

        		//loop through all http requests
        		for(int count = startOfItems; count <items; count++)
        		{
        			Http h = (Http) results.get(count);
        			initialiseContentOnTable(httpTable, h);
        			
        		}
		    }
        }
		Task task = new Task();			
	    task.execute();
	    
	    return rootView;
	}
	/**
	 * initialise headers
	 */
	private void initialiseHeaderName()
	{
		headerList.add("Date Time ");
		headerList.add("Pid ");
		headerList.add("Package Name ");
		headerList.add("Destination Address ");
		headerList.add("User Agent");
		headerList.add("Content Type ");
		headerList.add("Content Length ");
		headerList.add("Content Encoding ");
		headerList.add("Accept Encoding ");
		headerList.add("Method Type ");
		headerList.add("Parameter Size ");
		
		headerList.add("Contains IMEI ");
		headerList.add("Contains IMSI ");
		headerList.add("Contains Browser History ");
		headerList.add("Contains Phone Number ");
		headerList.add("Contains Email ");
	}
	/**
	 * initialise header names onto table row on UI
	 * @param httpTable
	 */
	private void initialiseHeadersOnTable(TableLayout httpTable)
	{
		TableRow tr = new TableRow(getActivity());
		TextView header0 = new TextView(getActivity());
		header0.setText(headerList.get(0));
		TextView header1 = new TextView(getActivity());
		header1.setText(headerList.get(1));
		TextView header2 = new TextView(getActivity());
		header2.setText(headerList.get(2));
		TextView header3 = new TextView(getActivity());
		header3.setText(headerList.get(3));
		TextView header4 = new TextView(getActivity());
		header4.setText("Click for more Info      ");

		
		tr.addView(header0);
		tr.addView(header1);
		tr.addView(header2);
		tr.addView(header3);
		tr.addView(header4);

		httpTable.addView(tr,  new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		httpTable.setStretchAllColumns(true);
		httpTable.bringToFront();
	}
	/**
	 * Initialise content retrieved from readFile method and display on table
	 * @param httpTable
	 * @param h
	 */
	private void initialiseContentOnTable(TableLayout httpTable, Http h)
	{
		TableRow tr =  new TableRow(getActivity());
		tr.setBackgroundDrawable( getResources().getDrawable(R.drawable.border));
	    LayoutParams tableRowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    tableRowParams.setMargins(3, 3, 2, 10);
	    tr.setLayoutParams(tableRowParams);
	
	    TextView c0 = new TextView(getActivity());
	    c0.setBackgroundDrawable( getResources().getDrawable(R.drawable.border));
	    c0.setText(" "+h.getDateTime()+" ");
	    
	    TextView c1 = new TextView(getActivity());
	    c1.setBackgroundDrawable( getResources().getDrawable(R.drawable.border));
	    c1.setText(" "+h.getPid()+" ");
	    
	    TextView c2 = new TextView(getActivity()); 	            
	    c2.setBackgroundDrawable( getResources().getDrawable(R.drawable.border));
	    c2.setText(" "+h.getSource()+" ");
	   
	    TextView c3 = new TextView(getActivity());
	    c3.setBackgroundDrawable( getResources().getDrawable(R.drawable.border));
	    c3.setText(" "+h.getDest()+" "); 
	      

		Spinner sp = new Spinner(getActivity());
		List<String> list = new ArrayList<String>();
		list.add("More info");
		list.add(headerList.get(4) +" - " +h.getUserAgent());
		list.add(headerList.get(5) +" - " +h.getContentType());
		list.add(headerList.get(6) +" - " +h.getContentLength());
		list.add(headerList.get(7) +" - " +h.getContentEncoding());
		list.add(headerList.get(8) +" - " +h.getAcceptEncoding());
		list.add(headerList.get(9) +" - " +h.getMethodType());
		list.add(headerList.get(10) +" - " +h.getParamSize());
		
		HashMap<String, Integer> specialAttributes = h.getSpecialAttribute();
	    if(specialAttributes.get("hasIMEI") == 1)
			list.add(headerList.get(11) +" - " +"True");
	    else
	    	list.add(headerList.get(11) +" - " +"False");
	    if(specialAttributes.get("hasIMSI") == 1)
	    	list.add(headerList.get(12) +" - " +"True");
	    else
	    	list.add(headerList.get(12) +" - " +"False");
	    if(specialAttributes.get("hasBrowserHistory") == 1)
	    	list.add(headerList.get(13) +" - " +"True");
	    else
	    	list.add(headerList.get(13) +" - " +"False");
	    if(specialAttributes.get("hasPhoneNumber") == 1)
	    	list.add(headerList.get(14) +" - " +"True");
	    else
	    	list.add(headerList.get(14) +" - " +"False");
	    if(specialAttributes.get("hasEmail") == 1)
	    	list.add(headerList.get(15) +" - " +"True");
	    else
	    	list.add(headerList.get(15) +" - " +"False");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(dataAdapter);

	    //add displays into table row
	    tr.addView(c0);
	    tr.addView(c1);
	    tr.addView(c2);
	    tr.addView(c3);
	    tr.addView(sp);
	    
	    //add rows into table
	    httpTable.addView(tr,  new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	//-----------------------------------------public method---------------------------------------------------
	/**
	 * get header of content data for export
	 * @return
	 */
	public ArrayList<String> getHeader()
	{
		return headerList;
	}
	/**
	 * get content data for export in httpfragment page
	 * @return
	 */
	public ArrayList getContentList()
	{
		return l;
	}
	/**
	 * Get instance of filehandler
	 * @return
	 */
	public FileHandler getFileHandlerInstance()
	{
		return fhdlr;
	}
}