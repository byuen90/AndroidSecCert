package com.example.log_istic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
 
public class SmsFragment extends Fragment  {
	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.sms_layout, container, false);
	        TableRow tr;
	        TableLayout httpTable = (TableLayout) rootView.getRootView().findViewById(R.id.tableLayoutSms);
	        tr = new TableRow(getActivity());
	        TextView header1 = new TextView(getActivity());
	        header1.setText("Source Number ");
	        TextView header2 = new TextView(getActivity());
	        header2.setText("Destination Number ");
	        TextView header3 = new TextView(getActivity());
	        header3.setText("Body Content ");

	        
	        tr.addView(header1);
	        tr.addView(header2);
	        tr.addView(header3);
	        
	        httpTable.addView(tr,  new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	        httpTable.setStretchAllColumns(true);
	        httpTable.bringToFront();
	        int pid = android.os.Process.myPid();
	        try
	        {
	        	File myFile = new File("/sdcard/logistic/logisticSMS.log");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				
				
	            int i = 0;
	            while ((aDataRow = myReader.readLine()) != null) {
		            tr =  new TableRow(getActivity());
		            tr.setId(100 + i);
		            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		            TextView c1 = new TextView(getActivity());
		            c1.setId(200 + i);
		            c1.setText(String.valueOf(i-1));
		            TextView c2 = new TextView(getActivity());
		            
		            	c2.setId(300 + i);
		            	c2.setText(aDataRow); 
	     		            
		            tr.addView(c1);
		            tr.addView(c2);
		           
		            httpTable.addView(tr,  new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		        }

	        }
	        catch (IOException e) 
	        {
	            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
	        }
	        
	        
	        return rootView;
	    }

}