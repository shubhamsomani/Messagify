package com.example.messagify;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Debug.Random;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Example calls 
		SpamFilterWrapper.TrainSpamFilter(this.getApplicationContext(),"data.arff");
		System.out.println(SpamFilterWrapper.ClassifySpam("prize of $300 to be won. call 9868041700.", this.getApplicationContext()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Move to inbox now
	public void GoToInbox(View view) {
	    Intent intent = new Intent(this, InboxActivity.class);
	    startActivity(intent);
	}
	
	public void GoToNewMessage(View view) {
	    Intent intent1 = new Intent(this, SentboxActivity.class);
	    startActivity(intent1);
	}
}
