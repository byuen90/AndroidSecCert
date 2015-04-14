package analyser;

public class ClassProb implements Comparable
{

    private String classLabel;
    private double probForThisClass;

    public ClassProb(String classLabel, double probForThisClass) 
    {
        this.classLabel = classLabel;
        this.probForThisClass = probForThisClass;
    }

    public String getClassLabel() 
    {
        return classLabel;
    }

    public void setClassLabel(String classLabel) 
    {
        this.classLabel = classLabel;
    }

    public double getProbForThisClass() 
    {
        return probForThisClass;
    }

    public void setProbForThisClass(double probForThisClass) 
    {
        this.probForThisClass = probForThisClass;
    }

     
    //sort ascending
    @Override
    public int compareTo(Object t) 
    {
        
        if (((ClassProb) t).getProbForThisClass() == getProbForThisClass())
        {
            return 0;
        }
        else if (((ClassProb) t).getProbForThisClass() < getProbForThisClass())
        {
            return 1;
        }
        else if (((ClassProb) t).getProbForThisClass() > getProbForThisClass())
        {
            return -1;
        }
        return 1;
    }

}
