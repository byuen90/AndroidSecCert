package com.example.log_istic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logreader.FileHandler;
import entity.Http;
import analyser.CsvHandler;
import analyser.KCrossValidator;
import analyser.NaiveBayes;
import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.common.collect.Lists;

public class HttpAnalysedFragment extends Fragment
{
	private ArrayList<String> headerList;
	private ArrayList contentList;
	private ArrayList<Object> arrTrained;
	private int fragmentSelected;
	private HashMap<String, Integer> selectionMap = new HashMap<String, Integer>();
	private HashMap<String, HashMap<Integer, Integer>> analysedApps = new HashMap<String, HashMap<Integer, Integer>>();
	Button resultButton;
	TextView displayResult;
	CsvHandler ch = new CsvHandler(); 
	KCrossValidator kcv = new KCrossValidator();
	KCrossValidator KCV;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		initialiseData();
		View.OnClickListener resultButtonListener = new OnClickListener() 
		{
			//clicklistener to handle result analysis
			public void onClick(View v) 
			{    
				class ResultTask extends AsyncTask<ArrayList<Float>, Void,ArrayList<Float>>
		        {
					ProgressDialog progress;
					//k-fold validation value
					int k = 10;
					String displayString = "";
					@Override
					protected void onPreExecute() {
						progress = new ProgressDialog(getActivity());
						progress.setMessage("Loading");
						progress.setIndeterminate(true);
						progress.setCancelable(false);
						progress.show();
				    }
					protected ArrayList<Float> doInBackground(ArrayList<Float>... rootView) 
					{
						//writing analysed data into training data set(CSV file)
						ch.generateCsvFile("Http", getAnalysedHeader(), getAnalysedContentList(), fragmentSelected);
						
						//based on the data retrieved in the naive bayes class, an analysis is conducted
						displayString = analyseResults(analysedApps);
						
						//conduct k-cross validation on training data
						KCV = new KCrossValidator(k, arrTrained, fragmentSelected);
						ArrayList<Float> tempPosNegValues = KCV.getPosNegValues();
						
						//return positive negative values to be used in onPostExecute()
						return tempPosNegValues;								
					} 
					protected void onPostExecute(ArrayList<Float> result) 
					{
						//calculate analysis based on positive negative values
						displayString += "K-Fold Validation (K = "+k+") Analysis : \n" + KCV.calculateAnalysis(result);
						
						//display on UI
						displayResult.append(displayString);	

		        		progress.dismiss();
				    }			
		        }
				ResultTask rtask = new ResultTask();			
				rtask.execute();
				
            }
        };
		View rootView = inflater.inflate(R.layout.httpanalyse_layout, container, false);
		resultButton = (Button) rootView.findViewById(R.id.buttonResults);
		resultButton.setOnClickListener(resultButtonListener);
		
		displayResult = (TextView) rootView.findViewById(R.id.textViewShowResults);
		
		//execute main update() method that conducts naive bayes algorithm on data
		rootView =  update(rootView);
        return rootView;
	}
	//--------------------------------------private methods------------------------------------------
	/**
	 * retrieve data from bundle, received from httpfragment passed by logactivity
	 */
	private void initialiseData()
	{
		//get bundle from httpfragment, contains content that requires analysis (From log files)
		Bundle bl = getArguments();				
		contentList = bl.getParcelableArrayList("Content");
		headerList = bl.getStringArrayList("Header");
		fragmentSelected = bl.getInt("Fragment Selected");
	}
	/**
	 * -reads trained model file from csv file
	 * -conduct naivebayes analysis
	 * -update view with analysed content from naivebayes
	 * @param rootView
	 * @return
	 */
	private View update(final View rootView)
	{		
		class UpdateTask extends AsyncTask<Void, Void,Void>
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
			protected Void doInBackground(Void... rootView) 
			{
	        	String fileName = "Http.csv";
	        	
	        	//read trained model from csv file (Refer to analyser/csvhandler class)
	        	arrTrained = ch.fileRead( fragmentSelected,  fileName);
	        	
	        	//conduct naivebayes analysis on new http testing data on trained model
				NaiveBayes nb = new NaiveBayes(fragmentSelected, arrTrained, contentList);
				
				//analysedApps is used in resultButtonListenerin onCreateView() method and initialisedTable() method 
				analysedApps = nb.initAnalyseApp();
				return null;
						
			} 
			protected void onPostExecute(Void result) 
			{    			
				
    			initialiseTable(rootView);  		
			
        		progress.dismiss();
		    }			
        }
		UpdateTask utask = new UpdateTask();			
	    utask.execute();
	    
	    return rootView;
	}
	/**
	 * initialise the headers of the table
	 * @param httpTable
	 */
	private void initialiseTable(View rootView)
	{
		//buttonslistener for user intervention to set correct malicious class label
		View.OnClickListener maliciousButtonListener = new OnClickListener() {
			public void onClick(View v) {
            	TableRow row = (TableRow)v.getParent() ;
            	int color = 0;
            	TextView appNameTextView = (TextView) row.getChildAt(0);
            	String appName = appNameTextView.getText().toString();

				int maliciousClass = 1;
				color = Color.RED;

				row.setBackgroundColor(color);
				selectionMap.put(appName, maliciousClass);
            }
        };
        View.OnClickListener notMaliciousButtonListener = new OnClickListener() {
			public void onClick(View v) {
            	TableRow row = (TableRow)v.getParent() ;
            	int color = 0;
            	TextView appNameTextView = (TextView) row.getChildAt(0);
            	String appName = appNameTextView.getText().toString();

				int maliciousClass = 0;
				color = Color.GREEN;
				
				row.setBackgroundColor(color);
				selectionMap.put(appName, maliciousClass);
            }
        };
        
		TableLayout httpTable = (TableLayout) rootView.getRootView().findViewById(R.id.tableLayoutHttpAnalyse);
		TableRow tr;
		tr = new TableRow(getActivity());
		TextView header1 = new TextView(getActivity());
		header1.setText("Application ");
		TextView header2 = new TextView(getActivity());
		header2.setText("Not Malicious ");
		TextView header3 = new TextView(getActivity());
		header3.setText("Malicious ");
		TextView header4 = new TextView(getActivity());
		header4.setText("Contains data ");
   		
		tr.addView(header1);
		tr.addView(header2);
		tr.addView(header3);
		tr.addView(header4);
		
		httpTable.addView(tr,  new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		httpTable.setStretchAllColumns(true);
		httpTable.bringToFront();
		
		//looping through the analysedapps hashmap
		Iterator it = analysedApps.entrySet().iterator();
		int count = 0;
	    while (it.hasNext()) 
	    {
	        Map.Entry application = (Map.Entry)it.next();
	        
	        //get values from map entry (how many http requests are malicious and how many are not)
	        HashMap<Integer, Integer> values = (HashMap<Integer, Integer>) application.getValue();
	        
	        Button btnMalicious = new Button(getActivity());  
	        btnMalicious.setText("Is Malicious");    
	        btnMalicious.setOnClickListener(maliciousButtonListener);
	        
	        Button btnNotMalicious = new Button(getActivity());  
	        btnNotMalicious.setText("Is Not Malicious");    
	        btnNotMalicious.setOnClickListener(notMaliciousButtonListener);
	        
			tr =  new TableRow(getActivity());
			tr.setBackgroundDrawable( getResources().getDrawable(R.drawable.border));
		    LayoutParams tableRowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		    tableRowParams.setMargins(3, 3, 2, 10);
		    tr.setLayoutParams(tableRowParams);
		
		    int color = 0;
		    
		    //not malicious class labels are more than malicious class label
		    if(values.get(0) > values.get(1) )
		    {        		    
		    	color = Color.GREEN;
		    	selectionMap.put((String)application.getKey(), 0);
		    }
		    else
		    {
		    	color = Color.RED;
		    	selectionMap.put((String)application.getKey(), 1);
		    }
		    
		    //used for special data
		    String specialData = "";
		    if(values.get(2) == 1)
		    	specialData += "Contains IMEI\n";
		    if(values.get(3) == 1)
		    	specialData += "Contains IMSI\n";
		    if(values.get(4) == 1)
		    	specialData += "Contains Browser History\n";
		    if(values.get(5) == 1)
		    	specialData += "Contains Phone Number\n";
		    if(values.get(6) == 1)
		    	specialData += "Contains Email\n";
		    TextView c1 = new TextView(getActivity());
		    //c1.setBackgroundColor(color);
		    c1.setText((String)application.getKey());
		    
		    
		    TextView c2 = new TextView(getActivity());
		    //c2.setBackgroundColor(color);        		    
		    c2.setText(values.get(0).toString());
		    
		    
		    TextView c3 = new TextView(getActivity()); 
		    //c3.setBackgroundColor(color);        		    
		    c3.setText(values.get(1).toString());
		    
		    TextView c4 = new TextView(getActivity()); 
		    //c3.setBackgroundColor(color);        		    
		    c4.setText(specialData);

		    tr.setBackgroundColor(color);
		    tr.addView(c1);
		    tr.addView(c2);
		    tr.addView(c3);
		    tr.addView(c4);
		    tr.addView(btnMalicious);
		    tr.addView(btnNotMalicious);
		    httpTable.addView(tr,  new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}
	
	/**
	 * analyse results to accumulate number of true/false positive/negatives through UI selection of user.  
	 * based on the selected correct class by the user from the UI, true/false positive/negatives will be calculated.
	 * @param tempAnalysedApps
	 */
	private String analyseResults(HashMap<String, HashMap<Integer, Integer>> tempAnalysedApps)
	{
		//initialise hashmap to store count
		HashMap<String, Integer> positiveNegativeCountMap = new HashMap<String, Integer>();
		positiveNegativeCountMap.put("True Positive", 0);
		positiveNegativeCountMap.put("False Positive", 0);
		positiveNegativeCountMap.put("True Negative", 0);
		positiveNegativeCountMap.put("False Negative", 0);
		
		//for each mobile application 
		Iterator it = tempAnalysedApps.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry pair = (Map.Entry)it.next();

	        //get application name
	        String appName = (String) pair.getKey() ;
	        /*selectionmap contains UI selection of "Correct" class, this value is based on user intervention on whether the
	        application is correct classified*/
	        int correctClass = selectionMap.get(appName);
	        
	        //get class labels of application
	        HashMap<Integer, Integer> tempClassifiedValueMap = (HashMap<Integer, Integer>) pair.getValue();
	        
	        //TABULATION OF TRUE/FALSE Postives/Negatives
	        //not malicious
	        if(correctClass == 0)
	        {
	        	//classified correctly
	        	int truePositive = positiveNegativeCountMap.get("True Positive");
	        	truePositive = truePositive + tempClassifiedValueMap.get(0);
	        	positiveNegativeCountMap.put("True Positive", truePositive);
	        	
	        	//classified wrongly
	        	int falseNegative = positiveNegativeCountMap.get("False Negative");
	        	falseNegative = falseNegative + tempClassifiedValueMap.get(1);
	        	positiveNegativeCountMap.put("False Negative", falseNegative);
	        }
	        else
	        {
	        	//classified wrongly
	        	int falsePositive = positiveNegativeCountMap.get("False Positive");
	        	falsePositive = falsePositive + tempClassifiedValueMap.get(0);
	        	positiveNegativeCountMap.put("False Positive", falsePositive);
	        	
	        	//classified correctly
	        	int trueNegative = positiveNegativeCountMap.get("True Negative");
	        	trueNegative = trueNegative + tempClassifiedValueMap.get(1);
	        	positiveNegativeCountMap.put("True Negative", trueNegative);
	        }

	        //make changes to malicious class of http entity to user defined class label
	        editMaliciousClass(appName, correctClass);
	    }//endwhile
	    
	    //get all values for calculation
	    float TP = positiveNegativeCountMap.get("True Positive");
	    float FN = positiveNegativeCountMap.get("False Negative");
	    float FP = positiveNegativeCountMap.get("False Positive");
	    float TN = positiveNegativeCountMap.get("True Negative");
	    
	    ArrayList<Float> posNegValues =  new ArrayList<Float>();
	    posNegValues.add(TP);
		posNegValues.add(FN);
		posNegValues.add(FP);
		posNegValues.add(TN);
		
		return "Current Analysis : \n"+kcv.calculateAnalysis(posNegValues) + "\n\n";		
	}
	/**
	 * Set http entity with correct class selected by user from UI
	 * this content list will be used to stored into trainedmodel file (CSV file)
	 * @param appName
	 * @param maliciousClass
	 */
	private void editMaliciousClass(String appName, int maliciousClass) 
	{
		
		int counter = 0;
		for(Object o : contentList)
		{
			Http h = (Http)o;
			String sourceName = h.getSource();
			if(sourceName.contentEquals(appName))
			{
				h.setMaliciousClass(maliciousClass);
			}
			contentList.set(counter, h);
			counter++;
		}
	}	
	
	//--------------------------------------public methods------------------------------------------
	/**
	 * retrieves analysed content list
	 * @return
	 */
	public ArrayList getAnalysedContentList()
	{
		return contentList;
	}
	/**
	 * retrieves header list
	 * @return
	 */
	public ArrayList<String> getAnalysedHeader()
	{
		return headerList;
	}

}