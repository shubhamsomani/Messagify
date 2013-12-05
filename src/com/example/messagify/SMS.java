/**
 * 
 */
package com.example.messagify;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SMS implements Parcelable {

	private int thread_id;
	private String address;
	private int person;
	private int _id;
	private Date date;
	private String body;
	private int read;
	private int type;

	/**
	 * Constructor
	 * @param _id			SMS Id
	 * @param thread_id		Thread Id, unique for a message thread with a unique person
	 * @param address		Mobile Number or address of the person
	 * @param person		Contact Id of the person which can be used to find the contact details
	 * @param date			Date in milliseconds format
	 * @param body			SMS text
	 * @param read			Read Status as defined in Android
	 */
	public SMS(int _id, int thread_id, String address, int person, Long date, String body, int read, int type) {
		this._id = _id;
		this.thread_id = thread_id;
		this.address = address;
		this.person = person;
		this.date = new Date(date);
		this.body = body;
		this.read = read;
		this.type = type;
	}

	/**
	 * Parcel Constructor
	 */
	@SuppressWarnings("deprecation")
	public SMS(Parcel input) {
		String[] data = new String[8];
		input.readStringArray(data);

		this._id = Integer.parseInt(data[0]);
		this.thread_id = Integer.parseInt(data[1]);
		this.address = data[2];
		this.person = Integer.parseInt(data[3]);
		this.date = new Date(data[4]);
		this.body = data[5];
		this.read = Integer.parseInt(data[6]);
		this.type = Integer.parseInt(data[7]);
	}

	/**
	 * Returns the date in proper string format to be displayed
	 * @return String
	 */
	public String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
		Date today = new Date();
		if(dateFormat.format(this.date).equals(dateFormat.format(today))) {
			SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
			return timeFormat.format(this.date);
		}
		else {
			SimpleDateFormat dayFormat = new SimpleDateFormat("MMM dd", Locale.US);
			return dayFormat.format(this.date);
		}
	}

	/**
	 * Returns the body of the text message
	 * @return String
	 */
	public String getMessage() {
		return this.body;
	}

	public String[] getContactInfo(Context context) {

		if(this.type == 1) {
			if(this.person == 0) {
				return new String[] {this.address, null};
			}
			else
			{
				String[] contactInfo = ContactUriHandler.findContactInfo(context, this.person, this.address);
				return contactInfo;
			}
		}
		else
		{
			String[] selfInfo = ContactUriHandler.findSelfInfo(context);
			return selfInfo;
		}
	}

	public int Read() {
		return this.read;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[]{
				String.valueOf(this._id),
				String.valueOf(this.thread_id),
				this.address,
				String.valueOf(this.person),
				String.valueOf(this.date),
				this.body,
				String.valueOf(this.read),
				String.valueOf(this.type)
			}
		);
	}

	public static final Parcelable.Creator<SMS> CREATOR= new Parcelable.Creator<SMS>() {

		@Override
		public SMS createFromParcel(Parcel source) {
			return new SMS(source);  //using parcelable constructor
		}

		@Override
		public SMS[] newArray(int size) {
			return new SMS[size];
		}
	};
}
