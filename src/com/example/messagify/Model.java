package com.example.messagify;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.Evaluation;
import java.util.Random;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.converters.ArffLoader.ArffReader;
import java.io.*;

import android.content.Context;


public class Model{
	
	/**
	* This method saves the classifier on the basis of which future classifications will be made.
	* @param fileName 		The name of the file that stores the dataset.
	* @param classifier 	The classifier of the program.
	* @param c 				The context of the program.
	*/	
	public void SaveModel(String fileName, FilteredClassifier classifier, Context c)
	{
		
		try{
			
			FileOutputStream fos = c.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(classifier);
			out.close();
			System.out.println("===== Saved model=====");
			
		}
		catch(Exception e){
			
			System.out.println("Problem found when writing");
            System.out.println(e.toString());
		}
	}
	
	

	/**
	* This method loads the classifier on the basis of which future classifications will be made.
	* @param fileName 		The name of the file that stores the dataset.
	* @param c 				The context of the program.
	* @return				The classifier.
	*/
    public FilteredClassifier loadModel(String fileName,Context c) {
        
    	FilteredClassifier classifier = null;
    	
    	try{
    		
        		FileInputStream fis =c.openFileInput(fileName);
                ObjectInputStream in = new ObjectInputStream(fis);
                Object tmp = in.readObject();
                classifier = (FilteredClassifier) tmp;
                in.close(); /**/
                System.out.println("===== Loaded model=====");
        } 
        catch (Exception e) {
        	
                System.out.println("Problem found when reading");
                System.out.println(e.toString());
        }

		return classifier;
}
	
	
}