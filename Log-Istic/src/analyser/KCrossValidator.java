package analyser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

public class KCrossValidator 
{
	ArrayList<Float> posNegValues = new ArrayList<Float>();
	/**
	 * empty constructor
	 */
	public KCrossValidator(){}
	
	/**
	 * Constructor to execute kcross validation. Takes in training data 
	 * @param k
	 * @param arrTrained
	 * @param fragmentSelected
	 */
	public KCrossValidator(int k, ArrayList<Object> arrTrained, int fragmentSelected)
	{
		
		float sizeOfModel = arrTrained.size();
		float avgSize = sizeOfModel/k;
		List<Object> xValidationList = arrTrained;
		
		//shuffles all entity to give a good mix of records throughout
		Collections.shuffle(xValidationList);
		
		//splits validation list into k equal parts (Using java Guava Lists Library)
		List<List<Object>> smallerList = Lists.partition(xValidationList, (int)avgSize);
		
		ArrayList<Object> trainingData = new ArrayList<Object>();
		ArrayList<Object> testingData = new ArrayList<Object>();
		HashMap<String, Integer> positiveNegativeCountMap = new HashMap<String, Integer>();
		
		float TP = 0;
	    float FN = 0;
	    float FP = 0;
	    float TN = 0;
	    
		int counter = 0;
		while(counter < k)
		{
			for(int i = 0; i<k; i++)
			{
				if(i == counter)
				{
					//assign only one set according to the counter for testing
					testingData = new ArrayList<Object>(smallerList.get(counter));
				}
				else
				{
					//assign all other lists into the training data
					trainingData.addAll(smallerList.get(i));
				}
			}
			//predicts using NaiveBayes
			NaiveBayes nb = new NaiveBayes(fragmentSelected, trainingData, testingData);
			
			//retrieve True and false : positive and negative counts
			positiveNegativeCountMap = nb.getPosNegCount();
			
			//add all the counts together
			TP += positiveNegativeCountMap.get("True Positive");
		    FN += positiveNegativeCountMap.get("False Negative");
		    FP += positiveNegativeCountMap.get("False Positive");
		    TN += positiveNegativeCountMap.get("True Negative");
		    
		    counter++;
		}
		
		//stores all values into data structure
		posNegValues.add(TP);
		posNegValues.add(FN);
		posNegValues.add(FP);
		posNegValues.add(TN);
		
	}
	
	/**
	 * returns the positive and negative values of kcross validation
	 * @return
	 */
	public ArrayList<Float> getPosNegValues()
	{
		return posNegValues;
	}
	
	/**
	 * conducts calculation of positive and negative values 
	 * @param posNegValues (arraylist)
	 * @return a result string
	 */
	public String calculateAnalysis(ArrayList<Float> posNegValues)
	{
		float TP = posNegValues.get(0);
		float FN = posNegValues.get(1);
		float FP = posNegValues.get(2);
		float TN = posNegValues.get(3);
		
	    double recall = (TP/(TP+FN))*100;	//biased towards C(YES|YES) & C(NO|YES)	    
        double accuracy = ((TP+TN)/(TP+TN+FP+FN))*100;
        double precision = (TP/(TP+FP))*100; //biased towards C(YES|YES) & C(YES|NO)
        double fMeasure = ((2*TP)/((2*TP)+FN+FP))*100; //biased towards all except C(NO|NO)
      
        String returnValue = "True Positive : "+TP+" True Negative : "+TN+" False Positive : "+FP+"  False Negative : "+FN+"\n"+
				"Accuracy : " + accuracy +" \n"+
				"Recall : " + recall + "\n" + 
				"Precision : " + precision + "\n" + 
				"F-Measure : " + fMeasure ;
        
        return returnValue;
	}
}
