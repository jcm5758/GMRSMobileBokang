package com.geurimsoft.bokangnew.apiserver.data;

import android.util.Log;

import com.geurimsoft.bokangnew.data.GSConfig;

import java.util.ArrayList;

public class GSDailyInOutGroup
{

    // 서비스 구분 : 입고, 출고, 토사
    public String ServiceType;

    // 전체 대수
    public int TotalCount;

    // 전체 수량
    public double TotalUnit;

    // 헤더
    public String[] Header;

    // 리스트
    public ArrayList<GSDailyInOutDetail> List = new ArrayList<GSDailyInOutDetail>();

    public GSDailyInOutGroup() {}

    public void add(GSDailyInOutDetail detail)
    {
        this.List.add(detail);
    }

    public String getTitle(int stateType)
    {
        if (stateType == GSConfig.STATE_AMOUNT)
            return this.getTitleUnit();
        else
            return this.getTitleMoney();

    }

    public String getTitleUnit()
    {
        return this.ServiceType + "(" + GSConfig.changeToCommanStringWOPoint(this.TotalCount) + "대 : " + GSConfig.changeToCommanString(this.TotalUnit) + "루베)";
    }

    public String getTitleMoney()
    {
        return this.ServiceType + "(" + GSConfig.changeToCommanStringWOPoint(this.TotalCount) + "대 : " + GSConfig.changeToCommanStringWOPoint(this.TotalUnit) + "천원)";
    }

    public GSDailyInOutDetail getDataFinal()
    {

        if (this.List.isEmpty())
            return null;

        return this.List.get(this.List.size() - 1);

    }

    public ArrayList<GSDailyInOutDetail> getDataWOFinal()
    {

        if (this.List.isEmpty())
            return null;

        ArrayList<GSDailyInOutDetail> result = new ArrayList<GSDailyInOutDetail>();

        for(int i = 0; i < this.List.size() - 1; i++)
            result.add(this.List.get(i));

        return result;

    }

}

