package com.example.messagify;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

public class Classification{
	
	
    /**
     * This method creates the instance to be classified, from the text that has been read.
     * @param	text		The text to be classified as Spam or not.
     */
    public Instances makeInstance(String text) {
    	
    		Instances instances;
    		
            // Create the attributes, class and text
            FastVector fvNominalVal = new FastVector(2);
            fvNominalVal.addElement("spam");
            fvNominalVal.addElement("ham");
            Attribute attribute1 = new Attribute("class", fvNominalVal);
            Attribute attribute2 = new Attribute("text",(FastVector) null);
            
            // Create list of instances with one element
            FastVector fvWekaAttributes = new FastVector(2);
            fvWekaAttributes.addElement(attribute1);
            fvWekaAttributes.addElement(attribute2);
            instances = new Instances("Test relation", fvWekaAttributes, 1);  
            
            // Set class index
            instances.setClassIndex(0);
            
            // Create and add the instance
            DenseInstance instance = new DenseInstance(2);
			instance.setValue(attribute2, text);
			
            instances.add(instance);
            System.out.println("===== Instance created with reference dataset =====");
            System.out.println(instances);
			return instances;
    }
    
    /**
     * This method performs the classification of the instance.
     * @param 	classifier		The Classifier on the basis of which classification has to be done.
     * @param	instances		Instances of features on the text to be classified.
     */
    public boolean classify(FilteredClassifier classifier,Instances instances) {
    	
    		double pred = 0;
            try {
         
					pred = classifier.classifyInstance(instances.instance(0));
                    System.out.println("===== Classified instance =====");
                    System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
                    
                    	
            }
            catch (Exception e) {
                    System.out.println("Problem found when classifying the text");
                    System.out.println(e.toString());
                    
            }

            if(instances.classAttribute().value((int) pred)=="spam")
            {
            	return true;                    		
            }
            else
            {
            	return false;
            }
            
    }
}