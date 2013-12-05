package com.example.messagify;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import android.content.Context;

public class SpamFilterWrapper
{
	/**
	* This method trains the Spam Filter according to the given Dataset.
	* @param  c 		Context of the Activity
	* @param  dataset	The name of the Dataset stored in /assets.
	*/	
	public static void TrainSpamFilter(Context c,String dataset){
		
		try{
		Instances trainData;
		FilteredClassifier classifier;
		
		DatasetLoader d1=new DatasetLoader();
		trainData = d1.load(dataset, c.getApplicationContext());
		
		Training t1=new Training();
		classifier = t1.learn(trainData,c.getApplicationContext());
		
		Model m1=new Model();
		m1.SaveModel("myClassifier.dat",classifier,c.getApplicationContext());
		}
		
		catch(Exception e){
			System.out.println("Problem in training spam filter @ Spam Filter Wrapper.");
			System.out.println(e.toString());
		}
		
	}

	/**
	* This method classifies a particular text and tells us whether it is spam or not.
	* @param  text		The text to be classified as Spam or not.
	* @param  c 		Context of the Activity
	* @return true		If text is Spam
	* @return false		If text is ham(not Spam).
	*/
	public static boolean ClassifySpam(String text,Context c){
		
		FilteredClassifier classifier = null;
		Classification c1 = null;
		Instances instances = null;
		
		try{
		Model m1=new Model();
		classifier=m1.loadModel("myClassifier.dat",c.getApplicationContext());
		
		c1=new Classification();
		instances=c1.makeInstance(text);
		
		}
		catch(Exception e){
			
			System.out.println("Problem in Classifying Spam @ Spam Filter Wrapper.");
			System.out.println(e.toString());
		}
		
		return c1.classify(classifier, instances);		
		
	}
}