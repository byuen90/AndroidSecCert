package analyser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;

import entity.Http;
//import other entity classes

/**
 * Class for conducting NaiveBayes
 */
public class NaiveBayes 
{
    private Hashtable<String, Integer> hashClassCount = new Hashtable();
    private Hashtable<String, Hashtable> hashClassType = new Hashtable();
    private Hashtable<String, Hashtable> hashAttr;
    //every attribute will be one tuple in this hashtable
    private Hashtable<String, Integer> hashValues;
    private int totalCount = 0;
    
    //Setting Class Labels
    private ArrayList<String> arrClassLabel = new ArrayList(Arrays.asList("0", "1"));

    //List to store testing data - Will be predicted and returned
    private ArrayList<Object> arrPredict;
    
    //Map to store all true/false positve/negative values for the test data
    private HashMap<String, Integer> positiveNegativeCountMap = new HashMap<String, Integer>();
    private int fragmentSelected;
    
    /**
     * naive bayes constructor, performs naive bayes classification
     * @param fragmentSel
     * @param arrTrained
     * @param arrTest
     */
    public NaiveBayes(int fragmentSel, ArrayList<Object> arrTrained, ArrayList<Object> arrTest) 
    {    	
    	//initialize
    	initPositiveNegativeMap();    	
    	arrPredict = arrTest; 			//assign testing data to arrPredict
    	fragmentSelected = fragmentSel; //assign choice of fragment selected
    	
    	//for HTTP
    	if(fragmentSelected == 0)
    	{
    		
            setupHashClassType();
            trainingModel(arrTrained);
            
            int counter = 0;
            
            for (Object test : arrTest) 
            {
            	Http hTest = (Http)test;
            	
            	//conduct prediction for test record
            	String classPrediction = predict(test);
            	String actualMaliciousClass = hTest.getMaliciousClass();

            	
            	calculatePositiveNegative(classPrediction, actualMaliciousClass);
            	//set predicted class label into arrPredict
            	hTest.setMaliciousClass(Integer.parseInt(classPrediction));
            	arrPredict.set(counter, test);
            	counter++;
            }

    	}
    	else if(fragmentSelected == 1)
    	{
    		//sms implementation loop through sms entity
    	}
    	else if(fragmentSelected == 2)
    	{
    	}
    }


    /**
     * initialise positiveNegativeCountMap with 0
     */
    private void initPositiveNegativeMap() 
    {
    	positiveNegativeCountMap.put("True Positive", 0);
		positiveNegativeCountMap.put("False Positive", 0);
		positiveNegativeCountMap.put("True Negative", 0);
		positiveNegativeCountMap.put("False Negative", 0);
	}
    
    /**
     * setting up the data structure for the model to hold all training data
     */
    private void setupHashClassType() 
    {
    	//for every classlabel, 0 or 1 (non malicious or malicious)
        for (String classLabel : arrClassLabel) 
        {
            Hashtable<String, Hashtable> hashAttr = new Hashtable();
            
            //for each of the classLabels, create a hashtable storing each attributes
            setupHashAttributesForEachClassType(hashAttr);
            
            //assigning attribute map object attributes assigned to each classlabel
            hashClassType.put(classLabel, hashAttr);
            
            //assign 0 counts to each class label
            hashClassCount.put(classLabel, 0);
        }
    }

    /**
     * initialise attributes into hashtable 
     * @param hashAttr
     */
    private void setupHashAttributesForEachClassType(Hashtable hashAttr) {
        hashValues = new Hashtable();
        hashAttr.put("Destination", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("User Agent", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Content Encoding", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Accept Encoding", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Content Type", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Content Length", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Method Type", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Parameter Size", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Has IMEI", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Has IMSI", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Has Browser History", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Has Phone Number", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Has Email", hashValues);
        hashValues = new Hashtable();
        hashAttr.put("Malicious Class", hashValues);
    }

    /**
     * Populating training model by updating occurance of each type of attribute value
     * @param arrRecords
     */
    private void trainingModel(ArrayList<Object> arrRecords) 
    {
        System.out.println("Training model");

        //loop through all training records (HTTP)
        if(fragmentSelected == 0)
        {
	        for (Object rec : arrRecords) 
	        {
	        	Http httpRec = (Http) rec;
	            String classType = httpRec.getMaliciousClass();
	
	            //count how many times each class label appears 
	            int classCount = hashClassCount.get(classType);
	            hashClassCount.put(classType, ++classCount);
	
	            //update 2nd layer hash with training record attribute value
	            updateAttrValue(classType, "Destination", httpRec.getDest());
	            updateAttrValue(classType, "User Agent", httpRec.getUserAgent());
	            updateAttrValue(classType, "Content Encoding", httpRec.getContentEncoding());
	            updateAttrValue(classType, "Accept Encoding", httpRec.getAcceptEncoding());
	            updateAttrValue(classType, "Content Type", httpRec.getContentType());
	            updateAttrValue(classType, "Content Length", httpRec.getContentLength());
	            updateAttrValue(classType, "Method Type", httpRec.getMethodType());
	            updateAttrValue(classType, "Parameter Size", httpRec.getParamSize());
	            HashMap<String, Integer> sAttributes = httpRec.getSpecialAttribute();
	            updateAttrValue(classType, "Has IMEI", Integer.toString(sAttributes.get("hasIMEI")));
	            updateAttrValue(classType, "Has IMSI", Integer.toString(sAttributes.get("hasIMSI")));
	            updateAttrValue(classType, "Has Browser History", Integer.toString(sAttributes.get("hasBrowserHistory")));
	            updateAttrValue(classType, "Has Phone Number", Integer.toString(sAttributes.get("hasPhoneNumber")));
	            updateAttrValue(classType, "Has Email", Integer.toString(sAttributes.get("hasEmail")));
	           
	            
	            hashAttr.put("Malicious Class", hashValues);
	        }
        }
        else if(fragmentSelected == 1)
        {
        	
        }
        else if(fragmentSelected == 2)
        {
        	
        }
    }

    /**
     * updates AttributeValue hashtables count 
     * adding count on occurance for each type of attribute for each class label
     * @param classType
     * @param attrName
     * @param attrValue
     */
    private void updateAttrValue(String classType, String attrName, String attrValue) 
    {
    	//gets the hash attribute according to class type (0 or 1, not malicious or malicious)
        hashAttr = hashClassType.get(classType);
        
        //get hashtable based on attribute name
        Hashtable currentAttr = hashAttr.get(attrName);

        //if does not contains this attribute value before, set count to 1
        if (!currentAttr.containsKey(attrValue)) 
        {        	
            currentAttr.put(attrValue, 1);
        } 
        else 
        {        	
            //if contains this attribute value before, increase the count
            int oldcount = (Integer) currentAttr.get(attrValue);
            currentAttr.put(attrValue, ++oldcount);
        }
    }


    /**
     * compute number of positive and negatives in the testing set, used for calculating recall, precision, f-measure, accuracy
     * @param classPrediction
     * @param actualMaliciousClass
     */
	private void calculatePositiveNegative(String classPrediction, String actualMaliciousClass) 
    {
    	//true negative
		if(classPrediction.equals(actualMaliciousClass) && classPrediction.equals("1"))
		{
			int trueNegative = positiveNegativeCountMap.get("True Negative");
        	trueNegative ++;
        	positiveNegativeCountMap.put("True Negative", trueNegative);
		}
		
		//true positive
		if(classPrediction.equals(actualMaliciousClass) && classPrediction.equals("0"))
		{
			int truePositive = positiveNegativeCountMap.get("True Positive");
			truePositive ++;
        	positiveNegativeCountMap.put("True Positive", truePositive);
		}
		
		if(!(classPrediction.equals(actualMaliciousClass)) && classPrediction.equals("1"))
		{
			int falseNegative = positiveNegativeCountMap.get("False Negative");
			falseNegative ++;
        	positiveNegativeCountMap.put("False Negative", falseNegative);
		}
		if(!(classPrediction.equals(actualMaliciousClass)) && classPrediction.equals("0"))
		{
			int falsePositive = positiveNegativeCountMap.get("False Positive");
			falsePositive ++;
        	positiveNegativeCountMap.put("False Positive", falsePositive);
		}
	}

	/**
	 * takes in one test record and predicts the class label based on the record's attribute and returns the classlabel with higer probability
	 * @param test
	 * @return
	 */
    private String predict(Object test) 
    {
    	//get total numbers of classes both malicious and non malicious
        totalCount = 0;
        for (int count : hashClassCount.values()) 
        {
            totalCount += count;
        }
        
        ArrayList<ClassProb> sort = new ArrayList();

        for (String classLabel : arrClassLabel) 
        {
            double probOfClass = checkProbForClassType(classLabel, test);
            if (Double.isNaN(probOfClass)) 
            {
                probOfClass = 0;
            }
            ClassProb classLabelProb = new ClassProb(classLabel, probOfClass);
            sort.add(classLabelProb);
        }
        //ascending order
        Collections.sort(sort);

        //return class label the larger probability
        return ((ClassProb) sort.get(sort.size() - 1)).getClassLabel();
    }

    /**
     * calculates probability of likehood to be classified as input classLabel
     * @param classType
     * @param testRec
     * @return
     */
    private double checkProbForClassType(String classType, Object testRec) 
    {
    	//get count of that classtype
        double classCount = hashClassCount.get(classType);

        //get the hashtable for that classtype
        hashAttr = hashClassType.get(classType);
        
        if(fragmentSelected == 0)
        {
        	double destCount = 0,userAgentCount = 0, cEncCount = 0, aEncCount = 0, ctCount = 0, clCount = 0, psCount = 0, mtCount = 0,
        			hasIMEICount = 0, hasIMSICount = 0, hasBHistoryCount = 0, hasPhoneNoCount = 0, hasEmailCount = 0;
        	Http httpTestRec = (Http) testRec;
	        //get hashtable with options for this hashAttr
	        hashValues = hashAttr.get("Destination");
	        if (hashValues.containsKey(httpTestRec.getDest())) {
	            //get count of attribute that matches testRecord
	            destCount = hashValues.get(httpTestRec.getDest());
	        }
	        hashValues = hashAttr.get("User Agent");
	        if (hashValues.containsKey(httpTestRec.getUserAgent())) {
	            //get count of attribute that matches testRecord
	            userAgentCount = hashValues.get(httpTestRec.getUserAgent());
	        }
	        hashValues = hashAttr.get("Content Encoding");
	        if (hashValues.containsKey(httpTestRec.getContentEncoding() + "")) {
	            //get count of attribute that matches testRecord
	        	cEncCount = hashValues.get(httpTestRec.getContentEncoding() + "");
	        }
	        hashValues = hashAttr.get("Accept Encoding");
	        if (hashValues.containsKey(httpTestRec.getAcceptEncoding() + "")) {
	            //get count of attribute that matches testRecord
	        	aEncCount = hashValues.get(httpTestRec.getAcceptEncoding() + "");
	        }
	        hashValues = hashAttr.get("Content Type");
	        if (hashValues.containsKey(httpTestRec.getContentType() + "")) {
	            //get count of attribute that matches testRecord
	            ctCount = hashValues.get(httpTestRec.getContentType() + "");
	        }
	        hashValues = hashAttr.get("Content Length");
	        if (hashValues.containsKey(httpTestRec.getContentLength() + "")) {
	            //get count of attribute that matches testRecord
	            clCount = hashValues.get(httpTestRec.getContentLength() + "");
	        }
	        hashValues = hashAttr.get("Method Type");
	        if (hashValues.containsKey(httpTestRec.getMethodType() + "")) {
	            //get count of attribute that matches testRecord
	            mtCount = hashValues.get(httpTestRec.getMethodType() + "");
	        }
	        hashValues = hashAttr.get("Parameter Size");
	        if (hashValues.containsKey(httpTestRec.getParamSize() + "")) {
	            //get count of attribute that matches testRecord
	            psCount = hashValues.get(httpTestRec.getParamSize() + "");
	        }
	        
	        HashMap<String, Integer> sAttributes = httpTestRec.getSpecialAttribute();

	        hashValues = hashAttr.get("Has IMEI");
	        if (hashValues.containsKey(Integer.toString(sAttributes.get("hasIMEI")) + "")) {
	            //get count of attribute that matches testRecord
	        	hasIMEICount = hashValues.get(Integer.toString(sAttributes.get("hasIMEI")) + "");
	        }
	        hashValues = hashAttr.get("Has IMSI");
	        if (hashValues.containsKey(Integer.toString(sAttributes.get("hasIMSI")) + "")) {
	            //get count of attribute that matches testRecord
	        	hasIMSICount = hashValues.get(Integer.toString(sAttributes.get("hasIMSI")) + "");
	        }
	        hashValues = hashAttr.get("Has Browser History");
	        if (hashValues.containsKey(Integer.toString(sAttributes.get("hasBrowserHistory")) + "")) {
	            //get count of attribute that matches testRecord
	        	hasBHistoryCount = hashValues.get(Integer.toString(sAttributes.get("hasBrowserHistory")) + "");
	        }
	        hashValues = hashAttr.get("Has Phone Number");
	        if (hashValues.containsKey(Integer.toString(sAttributes.get("hasPhoneNumber")) + "")) {
	            //get count of attribute that matches testRecord
	        	hasPhoneNoCount = hashValues.get(Integer.toString(sAttributes.get("hasPhoneNumber")) + "");
	        }
	        hashValues = hashAttr.get("Has Email");
	        if (hashValues.containsKey(Integer.toString(sAttributes.get("hasEmail")) + "")) {
	            //get count of attribute that matches testRecord
	        	hasEmailCount = hashValues.get(Integer.toString(sAttributes.get("hasEmail")) + "");
	        }
	        
	        //compute the probability or each attribute so see the likelyhood of that test object
	        //can test with more dimensions or lesser dimensions 
	      
	        return (destCount / classCount) * 
	        		(cEncCount / classCount) *
	        		(aEncCount / classCount) * 
	        		(ctCount / classCount)   * 
	        		(clCount / classCount)   *
	        		(psCount / classCount)   *
	        		(mtCount / classCount)   * 
	        		//(hasIMEICount / classCount) * 
	        		//(hasIMSICount / classCount)   * 
	        		//(hasBHistoryCount / classCount)   *
	        		//(hasPhoneNoCount / classCount)   *
	        		//(hasEmailCount / classCount)   * 
	        		(classCount / totalCount);
        }
        
        return 0;
    }

    
    
    //-----------------------------------public methods for front end UI---------------------------------------------
    /**
     * get predicted list of objects
     * @return
     */
    public ArrayList getContentList()
    {
    	return arrPredict;
    }
    
    /**
     * compute to total count of malicious/non malicious classes
     * add special attribute existence to hashmap
     * returns this set of data to UI
     * Used int HttpAnalysedFragment.java
     * @return
     */
    public HashMap<String, HashMap<Integer, Integer>> initAnalyseApp()
	{
		HashMap<String, HashMap<Integer, Integer>> tempAnalysedApps = new HashMap<String, HashMap<Integer, Integer>>();
		int contentLength = arrPredict.size();
		for(int i = 0; i < contentLength; i++)
		{
			Http h = (Http)arrPredict.get(i);
			String sourceName = h.getSource();
			int malClass = Integer.parseInt(h.getMaliciousClass());
			HashMap<String, Integer> specialAttributes = h.getSpecialAttribute();
			
			//hashmap tempanalysedapp does not contain application add 1 count into classMap
			if(!tempAnalysedApps.containsKey(sourceName))
			{
				HashMap<Integer, Integer> classMap = new HashMap<Integer, Integer>();
				if(malClass == 0)
				{
					classMap.put(0, 1);
					classMap.put(1, 0);
				}
				else
				{
					classMap.put(0, 0);
					classMap.put(1, 1);
				}
				
				classMap.put(2, specialAttributes.get("hasIMEI"));
				classMap.put(3, specialAttributes.get("hasIMSI"));
				classMap.put(4, specialAttributes.get("hasBrowserHistory"));
				classMap.put(5, specialAttributes.get("hasPhoneNumber"));
				classMap.put(6, specialAttributes.get("hasEmail"));
				
				tempAnalysedApps.put(sourceName, classMap);
			}
			else 
			{
	            //if contains this attribute value before, increase the count

				int oldcount;
				HashMap<Integer, Integer> tempClassTable = new HashMap<Integer, Integer>();
				tempClassTable = tempAnalysedApps.get(sourceName);
				if(specialAttributes.get("hasIMEI") == 1)
				 	tempClassTable.put(2,1);
    		    if(specialAttributes.get("hasIMSI") == 1)
    		    	tempClassTable.put(3,1);
    		    if(specialAttributes.get("hasBrowserHistory") == 1)
    		    	tempClassTable.put(4,1);
    		    if(specialAttributes.get("hasPhoneNumber") == 1)
    		    	tempClassTable.put(5,1);
    		    if(specialAttributes.get("hasEmail") == 1)
    		    	tempClassTable.put(6,1);
				if(malClass == 0)
				{
		            oldcount = (Integer) tempAnalysedApps.get(sourceName).get(0);
		            tempClassTable.put(0, ++oldcount);
		            tempAnalysedApps.put(sourceName, tempClassTable);
				}
				else
				{
					oldcount = (Integer) tempAnalysedApps.get(sourceName).get(1);
					tempClassTable.put(1, ++oldcount);
					tempAnalysedApps.put(sourceName, tempClassTable);
				}			            
	        }
		}
		return tempAnalysedApps;
	}
    
    /**
     * get method to get Positive and negative count map computed by calculatePositiveNegative()
     * @return
     */
    public HashMap<String, Integer> getPosNegCount()
    {
    	return positiveNegativeCountMap;
    }
}
