package com.example.messagify;

import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;

public class SMSActivity extends Activity {

	private int somethingHasChanged = 0;
	private SMSThread smsThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		// Show the Up button in the action bar.
		setupActionBar();

		this.smsThread = getIntent().getParcelableExtra("SMSThread");
		createView();
	}

	public void createView() {
		setTitle(this.smsThread.getContactInfo(this)[0]);

		CustomSMSAdapter adapter = new CustomSMSAdapter(this, this.smsThread.getSMSList());
		final ListView listView = (ListView) findViewById(R.id.msgList);
		listView.setAdapter(adapter);

		final Button button = (Button) findViewById(R.id.sendButton);
		final EditText editText = (EditText) findViewById(R.id.sendText);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	sendSMS(editText.getText().toString());
            	editText.setText(null);
            }
        });
	}

	public void sendSMS(String message) {
		SMSUriHandler.sendSMS(this, message, this.smsThread.getAddress(), this.smsThread.getThreadId());
		this.somethingHasChanged = 1;
		Date date = new Date();
		SMS newSms = new SMS(1, this.smsThread.getThreadId(), smsThread.getAddress(), smsThread.getPerson(), date.getTime(), message, 1, 2);
		this.smsThread.insertBackSMS(newSms);
		createView();
	}

	@Override
	public void onBackPressed() {
		if(this.somethingHasChanged == 1) {
			Intent i= new Intent(this, InboxActivity.class);
			this.finish();
			startActivity(i);
		}
		else {
			super.onBackPressed();
		}
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
		getMenuInflater().inflate(R.menu.sm, menu);
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
