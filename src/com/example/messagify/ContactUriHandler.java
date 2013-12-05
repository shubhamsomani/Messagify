package com.example.messagify;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

public class ContactUriHandler {

	private ContactUriHandler() {
		// // The constructor is private so that the object of this class can not be initialised.
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static String[] findContactInfo(Context context, int raw_contact_id, String address) {
		int contact_id = getContactId(context, raw_contact_id);
		String[] contactInfo = new String[] {null, null};
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] {ContactsContract.Contacts._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_THUMBNAIL_URI};

		Cursor cursor = context.getContentResolver().query(uri, projection, 
		        ContactsContract.Contacts._ID + " = ?",
		        new String[] { Integer.toString(contact_id) }, null);
	
		if (cursor.moveToFirst()) {
		    int has_phone_index = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		    int has_phone = cursor.getInt(has_phone_index);
		    if(has_phone == 1) {
		    	int display_name_index = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		    	contactInfo[0] = cursor.getString(display_name_index);
		    	int photo_thumbnail_uri_index = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
		    	contactInfo[1] = cursor.getString(photo_thumbnail_uri_index);
		    }
		    else {
		    	contactInfo[0] = address;
		    }
		}
		cursor.close();
		return contactInfo;
	}

	public static int getContactId(Context context, int raw_contact_id) {
		int contact_id = 0;
		Uri uri = ContactsContract.RawContacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.RawContacts._ID, ContactsContract.RawContacts.CONTACT_ID };
		Cursor cursor = context.getContentResolver().query(uri, projection, 
		        ContactsContract.RawContacts._ID + " = ?",
		        new String[] { Integer.toString(raw_contact_id) }, null);
	
		if (cursor.moveToFirst()) {
		    int contactIdIndex = cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);
		    contact_id = cursor.getInt(contactIdIndex);
		}
		cursor.close();
		return contact_id;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static String[] findSelfInfo(Context context) {
		String[] myInfo = new String[] {null, null};
		Uri uri = ContactsContract.Profile.CONTENT_URI;
		String[] projection = new String[] {ContactsContract.Profile._ID, ContactsContract.Profile.DISPLAY_NAME_PRIMARY, ContactsContract.Profile.PHOTO_THUMBNAIL_URI};

		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

		if (cursor.moveToFirst()) {
			int display_name_index = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			myInfo[0] = cursor.getString(display_name_index);
			int photo_thumbnail_uri_index = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
			if(photo_thumbnail_uri_index > 0) {
				myInfo[1] = cursor.getString(photo_thumbnail_uri_index);
			}
		}
		cursor.close();
		return myInfo;
	}
}
