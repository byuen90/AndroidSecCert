package com.example.log_istic;
	
import logreader.FileHandler;


import analyser.CsvHandler;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Log activity, contains all the fragment tabs in the UI
 * @author Bernard
 *
 */
public class LogActivity extends Activity {
	// Declare Tab Variable
	ActionBar.Tab smsTab, httpTab, phoneTab;
	Fragment smsFragmentTab = new SmsFragment();
	Fragment httpFragmentTab = new HttpFragment();
	Fragment phoneFragmentTab = new PhoneFragment();
	Fragment httpAnalysedFragmentTab = new HttpAnalysedFragment();
	Context context;
	MenuItem exportItem;
	MenuItem refreshItem;
	MenuItem analyseItem;
	private int fragmentSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		context = this;
		// Asking for the default ActionBar element that our platform supports.

        ActionBar actionBar = getActionBar();
        // Screen handling while hiding ActionBar icon.
        actionBar.setDisplayShowHomeEnabled(false);

        // Screen handling while hiding Actionbar title.

        actionBar.setDisplayShowTitleEnabled(false);

        // Creating ActionBar tabs.

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Setting custom tab icons.

        smsTab = actionBar.newTab().setIcon(R.drawable.icon_sms_tab);
        httpTab = actionBar.newTab().setIcon(R.drawable.icon_http_tab);
        phoneTab = actionBar.newTab().setIcon(R.drawable.icon_phone_tab);

        // Setting tab listeners.

        smsTab.setTabListener(new MyTabsListener(smsFragmentTab));
        httpTab.setTabListener(new MyTabsListener(httpFragmentTab));
        phoneTab.setTabListener(new MyTabsListener(phoneFragmentTab));
        
        // Adding tabs to the ActionBar.
        actionBar.addTab(smsTab);
        actionBar.addTab(httpTab);
        actionBar.addTab(phoneTab);

    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log, menu);
		exportItem =  menu.findItem(R.id.action_export);
    	refreshItem =  menu.findItem(R.id.action_refresh);
    	analyseItem =  menu.findItem(R.id.action_analyze);
    	
    	//set visibility for action button on action bar
        if(fragmentSelected == 0) 
        {
        	exportItem.setVisible(true);
        	analyseItem.setVisible(true);				
			refreshItem.setVisible(true);	        	        	
        }
        if(fragmentSelected == 1) 
        {
        	exportItem.setVisible(true);
        	analyseItem.setVisible(true);				
			refreshItem.setVisible(true);
        }
        if(fragmentSelected == 2) 
        {
        	exportItem.setVisible(true);
        	analyseItem.setVisible(true);				
			refreshItem.setVisible(true);
        }

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Context context = getApplicationContext();
		CharSequence text = "Export Complete";
		int duration = Toast.LENGTH_SHORT;
		Toast toast;

		HttpFragment ht = (HttpFragment)httpFragmentTab;
		SmsFragment st = (SmsFragment)smsFragmentTab;
		HttpAnalysedFragment hat = (HttpAnalysedFragment)httpAnalysedFragmentTab;
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		//get selected item id
		int id = item.getItemId();
		//export button for csv to add unclassified data into training data set
		if(id == R.id.action_export)
		{
			CsvHandler ch = new CsvHandler();
			if(fragmentSelected == 0)
			{				
				//export new content to csv file (Those in .log files)
				ch.generateCsvFile("Http", ht.getHeader(), ht.getContentList(), fragmentSelected);
				toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			//else other fragments
		}
		//conduct analysis of content from log file
		if(id == R.id.action_analyze)
		{
			//get instance from httpfragment to delete log files read in the fragment
			FileHandler fhdlr = ht.getFileHandlerInstance();
			//remove existing log files
    		fhdlr.removeLogFiles();
    		
    		//set all action button to false
			exportItem.setVisible(false);
			analyseItem.setVisible(false);				
			refreshItem.setVisible(false);
			if(fragmentSelected == 0)
			{
				//puts content into bundle to be passed to httpAnalysedFragment
				Bundle bl = new Bundle();
				bl.putInt("Fragment Selected", fragmentSelected);
				bl.putParcelableArrayList("Content", ht.getContentList());
				bl.putStringArrayList("Header", ht.getHeader());
				
				httpAnalysedFragmentTab.setArguments(bl);
				
				//removes httpfragment
				transaction.remove(ht);
				//starts httpanalysedfragment
				transaction.replace(R.id.activity_log, httpAnalysedFragmentTab);
				transaction.addToBackStack(null);
				transaction.commit();
			}
			//else other fragments
		}
		//refresh fragment with updated logs, remove and add fragment
		if(id == R.id.action_refresh)
		{
			if(fragmentSelected == 0)
			{
				transaction.remove(ht);
				transaction.replace(R.id.activity_log, ht);
				transaction.addToBackStack(null);
				transaction.commit();
			}
			//else other fragments
		}
		//settings button
		if (id == R.id.action_settings) 
		{
			//not implemented
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//tablistener class for fragments
	class MyTabsListener implements ActionBar.TabListener 
	{
	    public Fragment fragment;
	    public MyTabsListener(Fragment fragment) 
	    {
	        this.fragment = fragment;
	    }
	     
	    //redisplay action buttons after analysis fragment
	    public void onTabReselected(Tab tab, FragmentTransaction ft) 
	    {
	    	if(fragment instanceof HttpFragment) 
	    	{  
	        	exportItem.setVisible(true);
	        	analyseItem.setVisible(true);				
				refreshItem.setVisible(true);	
	        }
	        if(fragment instanceof SmsFragment) 
	        {
	        	exportItem.setVisible(true);
	        	analyseItem.setVisible(true);				
				refreshItem.setVisible(true);	
	        }
	        if(fragment instanceof PhoneFragment) 
	        {
	        	exportItem.setVisible(true);
	        	analyseItem.setVisible(true);				
				refreshItem.setVisible(true);	
	        }
	    }
	    
	    //set fragmentselected values used in all fragments to determine which fragment is the current selected
	    public void onTabSelected(Tab tab, FragmentTransaction ft) 
	    {
	        ft.replace(R.id.activity_log, fragment);

	        if(fragment instanceof HttpFragment) {
	        	fragmentSelected = 0;	  
	        }
	        if(fragment instanceof SmsFragment) {
	        	fragmentSelected = 1;
	        }
	        if(fragment instanceof PhoneFragment) {
	        	fragmentSelected = 2;
	        }
	    }
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	    {
	        ft.remove(fragment);
	    }
	}
}
