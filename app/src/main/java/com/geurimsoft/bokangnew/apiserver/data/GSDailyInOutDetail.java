package com.geurimsoft.bokangnew.apiserver.data;

import android.util.Log;

import com.geurimsoft.bokangnew.data.GSConfig;

public class GSDailyInOutDetail
{

    public String customerName;
    public int valueSize = 0;
    private double[] values = new double[10];

    public GSDailyInOutDetail() {}

    /**
     * 수량일 경우에는 values 반환
     * 금액일 경우에는 GSConfig.moneyDivideNum로 나누어서 반환
     * @param iUnitMoneyType
     * @return
     */
    public double[] getValues(int iUnitMoneyType)
    {

//        if (iUnitMoneyType == 0)
            return this.values;

//        double[] result = values;

//        for(int i = 0; i < result.length; i++)
//            result[i] = (result[i] / GSConfig.moneyDivideNum);

//        return result;

    }

    public String[] getStringValues(int iUnitMoneyType)
    {

        String[] result = new String[this.valueSize + 1];
        result[0] = this.customerName;

//        Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), "getStringValues()") + this.valueSize);

        for(int i = 0; i < this.values.length; i++)
        {

//            if (iUnitMoneyType == GSConfig.STATE_PRICE)
//                result[i + 1] = GSConfig.changeToCommanString(this.values[i] / GSConfig.moneyDivideNum);
//            else
                result[i + 1] = GSConfig.changeToCommanString(this.values[i]);

        }

        return result;

    }

    public void print()
    {

        Log.d(GSConfig.APP_DEBUG, "customerName : " + this.customerName);

        for(double value : this.values)
        {
            Log.d(GSConfig.APP_DEBUG, "valueSize : " + value);
        }

    }

}
