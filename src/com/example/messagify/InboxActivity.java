package com.example.messagify;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class InboxActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);

		// Once hash is ready, transfer data into ArrayList and de-initialize hash
		Map<Integer, SMSThread> inbox = SMSUriHandler.fetchInbox(this);
		
		final ArrayList<SMSThread> smsThreadList= new ArrayList<SMSThread>();

		for(Map.Entry<Integer, SMSThread> entry : inbox.entrySet()){
			  smsThreadList.add(entry.getValue());
		}
		inbox = null;

		CustomAdapter adapter = new CustomAdapter(this, smsThreadList);
		

		//Get the list view object from XML.
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);

        //On clicking the list
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {

            	String body = null;
            	GoToSMS(view, body, smsThreadList.get(position), position);

            }
        });

	}

	/**
	 * Listener on clicking a SMS
	 * @param view
	 * @param body
	 * @param smsThread		SMS Thread which was selected by the user
	 */
	public void GoToSMS(View view,String body, SMSThread smsThread, int position) {

		// Mark SMS thread as read if its unread
		if(smsThread.Read() == 0) {
			TextView textViewPreviewMessage = (TextView) view.findViewById(R.id.previewMessage);
		    TextView textViewContactName = (TextView) view.findViewById(R.id.contactName);
		    TextView textViewDate = (TextView) view.findViewById(R.id.date);
		    textViewContactName.setTypeface(null, Typeface.NORMAL);
	    	textViewPreviewMessage.setTypeface(null, Typeface.NORMAL);
	    	textViewDate.setTypeface(null, Typeface.NORMAL);

			SMSUriHandler.markThreadRead(this, smsThread.getThreadId());
		}

		Intent intent = new Intent(this, SMSActivity.class);
		intent.putExtra("SMSThread", smsThread);
		startActivity(intent);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inbox, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
