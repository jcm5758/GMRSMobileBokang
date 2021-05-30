package com.geurimsoft.bokangnew.data;

import com.geurimsoft.bokangnew.conf.AppConfig;

public class GSMonthInOutDetail
{

    public String day;

    public int valueSize = 0;
    public double[] values = new double[10];

    public GSMonthInOutDetail(){}

    public String[] getStringValues(int statType)
    {

        String[] result = new String[this.valueSize + 1];

        result[0] = this.day;

        for(int i = 0; i < this.valueSize; i++)
        {
            if (statType == GSConfig.STATE_AMOUNT)
                result[i + 1] = GSConfig.changeToCommanString(this.values[i]);
            else if (statType == AppConfig.STATE_PRICE)
                result[i + 1] = GSConfig.changeToCommanString(this.values[i] / AppConfig.moneyDivideNum);
        }

        return result;

    }

    public String[] getStringValuesForChart()
    {

        String[] result = new String[this.valueSize - 1];

        for(int i = 0; i < this.valueSize; i++)
        {

            if (i == (this.valueSize - 1))
                result[i] = GSConfig.changeToCommanString(this.values[i] + this.values[i + 1]);
            else
                result[i] = GSConfig.changeToCommanString(this.values[i]);

        }

        return result;

    }

    public double[] getDoubleValuesForChart()
    {

        double[] result = new double[this.valueSize - 1];

        for(int i = 0; i < result.length; i++)
        {

            if (i == (result.length - 1))
                result[i] = this.values[i] + this.values[i + 1];
            else
                result[i] = this.values[i];

        }

        return result;

    }

    public String[] getMoneyStringValues()
    {

        String[] result = new String[this.valueSize + 1];

        result[0] = this.day;

        for(int i = 0; i < this.valueSize; i++)
            result[i + 1] = GSConfig.changeToCommanString(this.values[i] / AppConfig.moneyDivideNum);

        return result;

    }

}
