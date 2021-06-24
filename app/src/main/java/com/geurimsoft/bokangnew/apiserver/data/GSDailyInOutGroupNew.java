package com.geurimsoft.bokangnew.apiserver.data;

import android.util.Log;

import com.geurimsoft.bokangnew.data.GSConfig;

import java.util.ArrayList;

public class GSDailyInOutGroupNew
{

    // 서비스 구분 : 입고, 출고, 토사
    public String serviceType;

    // list 데이터의 수
    public int recordCount;

    // 헤더의 수
    public int headerCount;

    // 전체 대수
    public int totalCount;

    // 전체 수량
    public double totalUnit;

    // 헤더
    public String[] header;

    // 리스트
    public ArrayList<GSDailyInOutDetailNew> list = new ArrayList<GSDailyInOutDetailNew>();

    public GSDailyInOutGroupNew() {}

    public void add(GSDailyInOutDetailNew detail)
    {
        this.list.add(detail);
    }

    public String getTitleUnit()
    {
        return this.serviceType + "(" + GSConfig.changeToCommanStringWOPoint(this.totalCount) + "대 : " + GSConfig.changeToCommanString(this.totalUnit) + "루베)";
    }

    public String getTitleMoney()
    {
        return this.serviceType + "(" + GSConfig.changeToCommanStringWOPoint(this.totalCount) + "대 : " + GSConfig.changeToCommanStringWOPoint(this.totalUnit) + "천원)";
    }

    public GSDailyInOutDetailNew getDataFinal()
    {

        if (this.list.isEmpty())
            return null;

        return this.list.get(this.list.size() - 1);

    }

    public ArrayList<GSDailyInOutDetailNew> getDataWOFinal()
    {

        if (this.list.isEmpty())
            return null;

        ArrayList<GSDailyInOutDetailNew> result = new ArrayList<GSDailyInOutDetailNew>();

        for(int i = 0; i < this.list.size() - 1; i++)
            result.add(this.list.get(i));

        return result;

    }

    public void print()
    {

        Log.d(GSConfig.APP_DEBUG, "ServiceType : " + this.serviceType);
        Log.d(GSConfig.APP_DEBUG, "recordCount : " + this.recordCount);
        Log.d(GSConfig.APP_DEBUG, "headerCount : " + this.headerCount);
        Log.d(GSConfig.APP_DEBUG, "totalCount : " + this.totalCount);
        Log.d(GSConfig.APP_DEBUG, "totalUnit : " + this.totalUnit);

        Log.d(GSConfig.APP_DEBUG, "Size of list : " + this.list.size());

        for(GSDailyInOutDetailNew detail : this.list)
        {
            detail.print();
        }

    }

}

