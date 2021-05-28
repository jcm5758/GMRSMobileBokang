package com.geurimsoft.bokangnew.data;

import android.util.Log;

import com.geurimsoft.bokangnew.conf.AppConfig;

import java.util.ArrayList;

public class GSMonthInOut {

    // list 데이터의 수
    public int recordCount;

    // 헤더의 수
    public int headerCount;

    // 헤더
    public String[] header;

    // 리스트
    public ArrayList<GSMonthInOutDetail> list = new ArrayList<GSMonthInOutDetail>();

    public GSMonthInOut() {
    }

    public void add(GSMonthInOutDetail detail) {
        this.list.add(detail);
    }

    /**
     * 합계 부분만
     *
     * @return
     */
    public GSMonthInOutDetail getFinalData() {

        if (this.list.size() == 0)
            return null;

        return this.list.get(this.list.size() - 1);

    }

    /**
     * 합계를 뺀 나머지 부분만
     *
     * @return
     */
    public ArrayList<GSMonthInOutDetail> getDataWOFinal()
    {

        if (this.list.size() == 0)
            return null;

        ArrayList<GSMonthInOutDetail> result = new ArrayList<GSMonthInOutDetail>();

        for (int i = 0; i < this.list.size() - 1; i++)
            result.add(this.list.get(i));

        return result;

    }

    public double[] getMonthChartData()
    {

        try
        {


            int doubleArraySize = (this.headerCount - 2) * (this.recordCount - 1);
            double[] result = new double[doubleArraySize];

            int max_index = 0;

            for(int i = 0; i < this.recordCount - 1; i++)
            {

                double[] items = this.list.get(i).getDoubleValuesForChart();

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
