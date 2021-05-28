package com.geurimsoft.grms.data;

import android.util.Log;

import com.geurimsoft.conf.AppConfig;

public class GSDailyInOutDetail
{

    public String customerName;
    public int valueSize = 0;
    private double[] values = new double[10];

    public GSDailyInOutDetail() {}

    /**
     * 수량일 경우에는 values 반환
     * 금액일 경우에는 AppConfig.moneyDivideNum로 나누어서 반환
     * @param iUnitMoneyType
     * @return
     */
    public double[] getValues(int iUnitMoneyType)
    {

        if (iUnitMoneyType == 0)
            return this.values;

        double[] result = values;

        for(int i = 0; i < result.length; i++)
            result[i] = (result[i] / AppConfig.moneyDivideNum);

        return result;

    }

    public String[] getStringValues(int iUnitMoneyType)
    {

        String[] result = new String[this.valueSize + 1];
        result[0] = this.customerName;

        for(int i = 0; i < this.values.length; i++)
        {

            if (iUnitMoneyType == AppConfig.STATE_PRICE)
                result[i + 1] = AppConfig.changeToCommanString(this.values[i] / AppConfig.moneyDivideNum);
            else
                result[i + 1] = AppConfig.changeToCommanString(this.values[i]);

        }

        return result;

    }

    public void print()
    {

        Log.d(AppConfig.TAG, "customerName : " + this.customerName);
        Log.d(AppConfig.TAG, "valueSize : " + this.valueSize);

        for(double value : this.values)
        {
            Log.d(AppConfig.TAG, "valueSize : " + value);
        }

    }

}
