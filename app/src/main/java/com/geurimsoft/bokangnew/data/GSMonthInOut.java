package com.geurimsoft.bokangnew.data;

import android.util.Log;

import java.util.ArrayList;

public class GSMonthInOut {

    // 헤더
    public String[] Header;

    // 리스트
    public ArrayList<GSMonthInOutDetail> List = new ArrayList<GSMonthInOutDetail>();

    public GSMonthInOut() {}


    /**
     * 합계 부분만
     *
     * @return
     */
    public GSMonthInOutDetail getFinalData() {

        if (this.List.size() == 0)
            return null;

        return this.List.get(this.List.size() - 1);

    }

    /**
     * 합계를 뺀 나머지 부분만
     *
     * @return
     */
    public ArrayList<GSMonthInOutDetail> getDataWOFinal()
    {

        if (this.List.size() == 0)
            return null;

        ArrayList<GSMonthInOutDetail> result = new ArrayList<GSMonthInOutDetail>();

        for (int i = 0; i < this.List.size() - 1; i++)
            result.add(this.List.get(i));

        return result;

    }

    public double[] getMonthChartData()
    {

        try
        {


            int doubleArraySize = (this.Header.length - 2) * (this.List.size() - 1);
            double[] result = new double[doubleArraySize];

            int max_index = 0;

            for(int i = 0; i < this.List.size() - 1; i++)
            {

                double[] items = this.List.get(i).getDoubleValuesForChart();

                for(int j = 0; j < items.length; j++)
                {
                    result[max_index] = items[j];
                    max_index++;
                }

            }

            return result;

        }
        catch(Exception ex)
        {
            Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : getMonthChartData() : " + ex.toString());
            return null;
        }

    }

}
