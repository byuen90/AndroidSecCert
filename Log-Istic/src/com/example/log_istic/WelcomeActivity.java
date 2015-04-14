package com.example.log_istic;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class WelcomeActivity extends Activity  {
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		progressBar = (ProgressBar) findViewById(R.id.progressBarWelcome);
		progressBar.setVisibility(View.GONE);
		addListenerOnButton();
	}

	
	public void addListenerOnButton()
	{
		final Context context = this;
		Button button = (Button) findViewById(R.id.buttonStart);
		final Task task = new Task();
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				progressBar.setVisibility(arg0.VISIBLE);
				task.execute(context);		
				
			}
		});
	}
	
	private class Task extends AsyncTask< Context, Integer, String> 
	{
	    @Override
	    protected String doInBackground(Context... con) 
	    {
	    	
	    	Intent toLogActivity = new Intent(con[0], LogActivity.class);
			startActivity(toLogActivity);
			return null;
	      
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	progressBar.setVisibility(View.GONE);
	    }
	  }
}
