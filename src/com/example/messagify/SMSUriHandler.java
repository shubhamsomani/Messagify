package com.example.messagify;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Static Class as all member functions are static, the object of this class should not be instantiated
 * @author Dhruv
 *
 */
public class SMSUriHandler {

	private final static String SMS_URI = "content://sms";
	private final static String SMS_INBOX_URI = "content://sms/inbox";
	private final static String SMS_SENT_URI = "content://sms/sent";

	private SMSUriHandler() {
		// The constructor is private so that the object of this class can not be initialised.
	}

	/**
	 * Fetch All SMS using URI Parser
	 * @param context
	 * @return LinkedHashMap<Integer, SMSThread> hash which has thread_id has Integer and correspnding sms thread
	 */
	public static LinkedHashMap<Integer, SMSThread> fetchInbox(Context context) {
		
		Uri uriSms = Uri.parse(SMS_URI);
		Cursor cursor = context.getContentResolver().query(uriSms, new String[]{"_id", "thread_id", "address", "person", "date", "body", "read", "type"},null,null,null);

		// Create a hash for each thread_id which will be linked to corresponding SMSThread object
		// Use LinkedHashMap because it maintains the order in which messages are inserted
		// which ensures the latest thread stays on top
		LinkedHashMap<Integer, SMSThread> hash = new LinkedHashMap<Integer, SMSThread>();

		while (cursor.moveToNext()) {
			
			//Read cursor details
			int _id = cursor.getInt(0);
			int thread_id = cursor.getInt(1);
			String address = cursor.getString(2);
			int person = cursor.getInt(3);
			Long date = cursor.getLong(4);
			String body = cursor.getString(5);
			int read = cursor.getInt(6);
			int type = cursor.getInt(7);

			// Initialize SMS object
			SMS sms = new SMS(_id, thread_id, address, person, date, body, read, type);
			//Log.e("Object Initialized", "sms details: " + sms.getPreviewMessage());

			// Check if hash exists for particular thread_id
			if(hash.containsKey(thread_id)) {
				hash.get(thread_id).insertSMS(sms);
			}
			else {
				SMSThread smsThread = new SMSThread(thread_id, address, person, sms);
				hash.put(thread_id, smsThread);
				//Log.e("Object Initialized", "hash details: " + hash.get(thread_id).getSMSThreadPreview());
			}
		}
		cursor.close();

		return hash;
	}

	/**
	 * Mark last unread messages in a thread as read
	 * @param context
	 * @param thread_id
	 */
	public static void markThreadRead(Context context, int thread_id) {

		Uri uri = Uri.parse(SMS_INBOX_URI);
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		while (cursor.moveToNext()) {
			if (cursor.getInt(cursor.getColumnIndex("thread_id")) == (thread_id)) {

				if (cursor.getInt(cursor.getColumnIndex("read")) == 0) {
					String SmsMessageId = cursor.getString(cursor.getColumnIndex("_id"));
					Log.e("MarkThreadRead", "Message Id: " + SmsMessageId);
					ContentValues values = new ContentValues();
					values.put("read", true);
					context.getContentResolver().update(Uri.parse(SMS_INBOX_URI), values, "_id=" + SmsMessageId, null);
				}
				else {
					return;
				}
			}
		}
		cursor.close();
	}

	public static void addToSent(Context context, String message, String phoneNumber, int thread_id) {
		ContentValues values = new ContentValues();
	    values.put("address", phoneNumber);
	    values.put("body", message);
	    values.put("thread_id", Integer.toString(thread_id));
	    context.getContentResolver().insert(Uri.parse("content://sms/sent"), values); 
	}

	public static void sendSMS(Context context, final String message, final String phoneNumber, final int thread_id) {
		String SENT = "sent";
		String DELIVERED = "delivered";
		final int duration = Toast.LENGTH_LONG;

		if(message == null) {
			return;
		}

		PendingIntent sent = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
		PendingIntent delivered = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

		context.registerReceiver(new BroadcastReceiver(){
		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	switch(getResultCode()) {
			    	case Activity.RESULT_OK:
	                    Toast.makeText(context, "SMS sent", 
	                            duration).show();
	                    addToSent(context, message, phoneNumber, thread_id);
	                    break;
	                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	                    Toast.makeText(context, "Generic failure", 
	                            duration).show();
	                    break;
	                case SmsManager.RESULT_ERROR_NO_SERVICE:
	                    Toast.makeText(context, "No service", 
	                            duration).show();
	                    break;
	                case SmsManager.RESULT_ERROR_NULL_PDU:
	                    Toast.makeText(context, "Null PDU", 
	                            duration).show();
	                    break;
	                case SmsManager.RESULT_ERROR_RADIO_OFF:
	                    Toast.makeText(context, "Radio off", 
	                            duration).show();
	                    break;
                }
		    }
		}, new IntentFilter(SENT));

		context.registerReceiver(new BroadcastReceiver(){
		    @Override
		    public void onReceive(Context context, Intent intent) {
				Toast.makeText(context, "SMS delivered", 
				duration).show();
		    }
		}, new IntentFilter(DELIVERED));


		SmsManager sms = SmsManager.getDefault();
		ArrayList<String> msgParts = sms.divideMessage(message);
		sms.sendTextMessage(phoneNumber, null, msgParts.get(0), sent, delivered);

		// Recursive call to send multiple sms's
		if(msgParts.size() > 1) {
			msgParts.remove(0);
			String remainingMessage = null;

			for(int i=0; i<msgParts.size(); ++i) {
				remainingMessage = remainingMessage + msgParts.get(i);
			}

			sendSMS(context, remainingMessage, phoneNumber, thread_id);
		}
	}
}
