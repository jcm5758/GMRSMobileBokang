package com.geurimsoft.grms.data;

import android.util.Log;

import com.geurimsoft.conf.AppConfig;

import java.util.ArrayList;

public class GSDailyInOutGroup
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
    public ArrayList<GSDailyInOutDetail> list = new ArrayList<GSDailyInOutDetail>();

    public GSDailyInOutGroup() {}

    public void add(GSDailyInOutDetail detail)
    {
        this.list.add(detail);
    }

    public String getTitleUnit()
    {
        return this.serviceType + "(" + AppConfig.changeToCommanStringWOPoint(this.totalCount) + "대 : " + AppConfig.changeToCommanString(this.totalUnit) + "루베)";
    }

    public String getTitleMoney()
    {
        return this.serviceType + "(" + AppConfig.changeToCommanStringWOPoint(this.totalCount) + "대 : " + AppConfig.changeToCommanStringWOPoint(this.totalUnit / AppConfig.moneyDivideNum) + "천원)";
    }

    public GSDailyInOutDetail getDataFinal()
    {

        if (this.list.isEmpty())
            return null;

        return this.list.get(this.list.size() - 1);

    }

    public ArrayList<GSDailyInOutDetail> getDataWOFinal()
    {

        if (this.list.isEmpty())
            return null;

        ArrayList<GSDailyInOutDetail> result = new ArrayList<GSDailyInOutDetail>();

        for(int i = 0; i < this.list.size() - 1; i++)
            result.add(this.list.get(i));

        return result;

    }

    public void print()
    {

        Log.d(AppConfig.TAG, "ServiceType : " + this.serviceType);
        Log.d(AppConfig.TAG, "recordCount : " + this.recordCount);
        Log.d(AppConfig.TAG, "headerCount : " + this.headerCount);
        Log.d(AppConfig.TAG, "totalCount : " + this.totalCount);
        Log.d(AppConfig.TAG, "totalUnit : " + this.totalUnit);

        Log.d(AppConfig.TAG, "Size of list : " + this.list.size());

        for(GSDailyInOutDetail detail : this.list)
        {
            detail.print();
        }

    }

}

