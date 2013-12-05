package com.example.messagify;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class SMSThread implements Parcelable {
	private int thread_id;
	private String address;
	private int person;
	private ArrayList<SMS> SMSList = new ArrayList<SMS>();

	/**
	 * Constructor
	 * @param thread_id 	Thread Id, unique for a message thread with a unique person
	 * @param address 		Mobile Number or address of the person
	 * @param person 		Contact Id of the person which can be used to find the contact details
	 * @param sms 			SMS Message
	 */
	public SMSThread(int thread_id, String address, int person, SMS sms) {
		this.thread_id = thread_id;
		this.address = address;
		this.person = person;
		this.insertSMS(sms);
	}

	public SMSThread() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Insert the SMS into the SMSList
	 * Latest sms are fetched first via Android API.
	 * This insertion function ensures that the latest sms is stored as the last element
	 * @param sms	SMS Message
	 */
	public void insertSMS(SMS sms) {
		this.SMSList.add(0, sms);
	}

	public void insertBackSMS(SMS sms) {
		this.SMSList.add(sms);
	}
	/**
	 * Return the Preview of the Last element (latest message) in the List
	 * @return
	 */
	public String getSMSThreadPreview() {
		return this.SMSList.get(SMSList.size() - 1).getMessage();
	}

	/**
	 * Return the date of the Last element (latest message) in the List
	 * @return
	 */
	public String getLatestDate() {
		return this.SMSList.get(SMSList.size() - 1).getDate();
	}

	public String[] getContactInfo(Context context) {
		if(this.person == 0) {
			return new String[] {this.address, null};
		}
		else
		{
			String[] contactInfo = ContactUriHandler.findContactInfo(context, this.person, this.address);
			return contactInfo;
		}
	}

	public int Read() {
		return this.SMSList.get(SMSList.size() - 1).Read();
	}

	public ArrayList<SMS> getSMSList() {
		return this.SMSList;
	}

	public int getNoMessages() {
		return this.SMSList.size();
	}

	public int getThreadId() {
		return this.thread_id;
	}

	public int getPerson() {
		return this.person;
	}

	public String getAddress() {
		return this.address;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(this.thread_id);
		dest.writeString(this.address);
		dest.writeInt(this.person);
		Bundle b = new Bundle();
		b.putParcelableArrayList("SMSList", SMSList);
		dest.writeBundle(b);
	}

	public static final Parcelable.Creator<SMSThread> CREATOR = new Parcelable.Creator<SMSThread>() { 

		@Override
		public SMSThread createFromParcel(Parcel in) { 

			SMSThread smsThread = new SMSThread();

			smsThread.thread_id = in.readInt();
			smsThread.address = in.readString();
			smsThread.person = in.readInt();

			Bundle b = in.readBundle(SMS.class.getClassLoader());
			smsThread.SMSList = b.getParcelableArrayList("SMSList");
	
			return smsThread;
		}

		@Override
		public SMSThread[] newArray(int size) {
			return new SMSThread[size];
		}
	};
}
