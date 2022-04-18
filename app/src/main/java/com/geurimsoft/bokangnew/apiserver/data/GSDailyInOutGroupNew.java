package com.geurimsoft.bokangnew.apiserver.data;

import android.util.Log;

import com.geurimsoft.bokangnew.data.GSConfig;

import java.util.ArrayList;

public class GSDailyInOutGroupNew
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
    public ArrayList<GSDailyInOutDetailNew> List = new ArrayList<GSDailyInOutDetailNew>();

    public GSDailyInOutGroupNew() {}

    public void add(GSDailyInOutDetailNew detail)
    {
        this.List.add(detail);
    }

    public String getTitleUnit()
    {
        return this.ServiceType + "(" + GSConfig.changeToCommanStringWOPoint(this.TotalCount) + "대 : " + GSConfig.changeToCommanString(this.TotalUnit) + "루베)";
    }

    public String getTitleMoney()
    {
        return this.ServiceType + "(" + GSConfig.changeToCommanStringWOPoint(this.TotalCount) + "대 : " + GSConfig.changeToCommanStringWOPoint(this.TotalUnit) + "천원)";
    }

    public GSDailyInOutDetailNew getDataFinal()
    {

        if (this.List.isEmpty())
            return null;

        return this.List.get(this.List.size() - 1);

    }

    public ArrayList<GSDailyInOutDetailNew> getDataWOFinal()
    {

        if (this.List.isEmpty())
            return null;

        ArrayList<GSDailyInOutDetailNew> result = new ArrayList<GSDailyInOutDetailNew>();

        for(int i = 0; i < this.List.size() - 1; i++)
            result.add(this.List.get(i));

        return result;

    }


}

