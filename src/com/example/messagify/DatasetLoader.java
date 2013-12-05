package com.example.messagify;

import java.io.*;
import java.util.*;

import android.content.Context;
import android.content.res.AssetManager;
import weka.classifiers.*;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

/* dev notes
loadDataset("sms-spam.arff")
*/

public class DatasetLoader
{
	/**
     * Object that stores training data.
     */
    Instances trainData;
	
	/**
     * TODO (Optional)
     * Add a method which loads data from text format and converts it into ARFF.
     */
    
	
	/**
    * This method loads a dataset in ARFF format. If the file does not exist, or
    * it has a wrong format, the attribute trainData is null.
    * @param  fileName 		The name of the file that stores the dataset.
    */
    public Instances load(String fileName,Context c) {
            try {
					//Open training data stored in /assets using Asset Manager.
					AssetManager am = c.getAssets();
					InputStream is = am.open(fileName);
					
					//Create a reader object for the input stream.
					Reader reader = new InputStreamReader(is);
					trainData =  new Instances(reader);
                 
                    System.out.println("===== Loaded dataset =====");
                    reader.close();
            }
            catch (IOException e) {
			
					System.out.println(e.toString());
                    System.out.println("Problem found when reading");
            }
			
			return trainData;
    }
    
    
}