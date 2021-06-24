package com.geurimsoft.bokangnew.apiserver.data;

import android.util.Log;

import com.geurimsoft.bokangnew.data.GSConfig;

public class GSDailyInOutDetailNew
{

    public String customer;
    private int count;
    private double[] values = new double[10];

    public GSDailyInOutDetailNew() {}

    public double[] getValues()
    {
        return this.values;
    }

    public String[] getStringValues()
    {

        String[] result = new String[ values.length + 1 ];
        result[0] = customer;

        for(int i = 0; i < this.values.length; i++)
        {
            result[i + 1] = GSConfig.changeToCommanString(this.values[i]);
        }

        return result;

    }

    public void print()
    {

        for(double value : this.values)
        {
            Log.d(GSConfig.APP_DEBUG, "valueSize : " + value);
        }

    }

}
