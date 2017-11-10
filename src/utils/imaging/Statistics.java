package utils.imaging;

import java.util.ArrayList;

public class Statistics
{
    private ArrayList<Double> averages;
    private ArrayList<Double> variances;

    public Statistics()
    {
        averages = new ArrayList<>();
        variances = new ArrayList<>();
    }

    public ArrayList<Double> getAverages()
    {
        return averages;
    }

    public void setAverage(int index, double average)
    {
        if (this.averages.size() >= index)
        {
            this.averages.add(index, average);
        }
        else
        {
            this.averages.set(index, average);
        }
    }

    public ArrayList<Double> getVariances()
    {
        return variances;
    }

    public void setVariance(int index, double variances)
    {
        if (this.variances.size() >= index)
        {
            this.variances.add(index, variances);
        }
        else
        {
            this.variances.set(index, variances);
        }
    }
}
