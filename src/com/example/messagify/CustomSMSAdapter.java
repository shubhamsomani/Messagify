package com.example.messagify;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSMSAdapter extends BaseAdapter {
	private final Context context;
	private final ArrayList<SMS> smsList;

	public CustomSMSAdapter(Context context, ArrayList<SMS> smsList) {
	    this.context = context;
	    this.smsList = smsList;
	}

	@Override
	public int getCount() {
		return this.smsList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.smsList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.thread_item_layout, parent, false);

	    TextView textViewMessage = (TextView) rowView.findViewById(R.id.message);
	    TextView textViewDate = (TextView) rowView.findViewById(R.id.date);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

	    textViewMessage.setText(this.smsList.get(position).getMessage());
	    textViewDate.setText(this.smsList.get(position).getDate());

	    String[] contactInfo = this.smsList.get(position).getContactInfo(this.context);
	    if(contactInfo[1] != null) {
	    	Uri uri = Uri.parse(contactInfo[1]);
	    	imageView.setImageURI(uri);
	    }

	    

	    return rowView;
	}
}
