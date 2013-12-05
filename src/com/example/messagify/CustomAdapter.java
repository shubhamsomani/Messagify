package com.example.messagify;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	private final Context context;
	private final ArrayList<SMSThread> smsThreadList;

	public CustomAdapter(Context context, ArrayList<SMSThread> smsThreadList) {
	    this.context = context;
	    this.smsThreadList = smsThreadList;
	}

	@Override
	public int getCount() {
		return this.smsThreadList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.smsThreadList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.inbox_item_layout, parent, false);

	    TextView textViewPreviewMessage = (TextView) rowView.findViewById(R.id.previewMessage);
	    TextView textViewContactName = (TextView) rowView.findViewById(R.id.contactName);
	    TextView textViewDate = (TextView) rowView.findViewById(R.id.date);
	    TextView textViewNoMessages = (TextView) rowView.findViewById(R.id.NoMessages);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

	    textViewPreviewMessage.setText(this.smsThreadList.get(position).getSMSThreadPreview());

	    String[] contactInfo = this.smsThreadList.get(position).getContactInfo(this.context);
	    textViewContactName.setText(contactInfo[0]);
	    textViewDate.setText(this.smsThreadList.get(position).getLatestDate());

	    textViewNoMessages.setText(Integer.toString(this.smsThreadList.get(position).getNoMessages()));

	    if(this.smsThreadList.get(position).Read() == 0) {
	    	textViewContactName.setTypeface(null, Typeface.BOLD);
	    	textViewPreviewMessage.setTypeface(null, Typeface.BOLD);
	    	textViewDate.setTypeface(null, Typeface.BOLD);
	    }
	    
	    if(contactInfo[1] != null) {
	    	Uri uri = Uri.parse(contactInfo[1]);
	    	imageView.setImageURI(uri);
	    }

	    return rowView;
	}
}
