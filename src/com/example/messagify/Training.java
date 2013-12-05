package com.example.messagify;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import weka.classifiers.*;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.Debug.Random;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.*;

public class Training{
	
	
	/**
     * Feature vector
     * Converts String attributes into a set of attributes representing word occurrence 
     * (depending on the tokenizer) information from the text contained in the strings.
     */	
	 StringToWordVector filter;
	
	 FilteredClassifier classifier;


	
	/**
    * This function trains the Classifier on the sample Dataset.
    * @param	trainData		The training data.
    * @param	c				The context of the Application.
	* @return 	FilteredClassifier
    */   
    public FilteredClassifier learn(Instances trainData,Context c){    	
    	    	
        try {

            trainData.setClassIndex(0);
            filter = new StringToWordVector();
            filter.setAttributeIndices("last");
            classifier = new FilteredClassifier();
            classifier.setFilter(filter);
            classifier.setClassifier(new NaiveBayes());
            classifier.buildClassifier(trainData);
            //System.out.println(classifier);
            System.out.println("===== Training on filtered (training) dataset done =====");

            
            
        }
        catch (Exception e) {
        	
        	System.out.println(e.toString());
            System.out.println("Problem found when training");
        }

		return classifier;
}
	
	
	/**
    * This function tells the statistics of the Naive Bayes Classifier.
    * It tells us how accurate its predictions are and so on.
    */
	public void Evaluate(Instances trainData){
		
		/**
	     * Feature vector
	     * Converts String attributes into a set of attributes representing word occurrence 
	     * (depending on the tokenizer) information from the text contained in the strings.
	     */
		//StringToWordVector filter;
		
		//FilteredClassifier classifier;
		
		try{
			
			//The Index at which class attributes {spam,ham} are stored in the database
			trainData.setClassIndex(0);
			
			
			filter = new StringToWordVector();
            filter.setAttributeIndices("last");
            
            
            //make a classifier on the filtered information. Using Naive Bayes classification here.
            classifier = new FilteredClassifier();
            classifier.setFilter(filter);
            classifier.setClassifier(new NaiveBayes());
            
            System.out.println(classifier);
            //System.out.println("hogya");
            
            //Evaluate the data based on the classifier.
            Evaluation eval = new Evaluation(trainData);
            eval.crossValidateModel(classifier, trainData, 4, new Random(1));
			
            
            System.out.println(eval.toSummaryString());
            System.out.println(eval.toClassDetailsString());

            System.out.println("Evaluation complete :) ");
			
		}
		catch (Exception e) {
			
    		System.out.println(e.toString());
            System.out.println("Problem found when evaluating");
    }
		
	}
	
}