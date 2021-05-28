package com.geurimsoft.grms.data;

import android.util.Log;

import com.geurimsoft.conf.AppConfig;

import java.util.ArrayList;

public class GSDailyInOut
{

    public String sDate;
    public ArrayList<GSDailyInOutGroup> list = new ArrayList<GSDailyInOutGroup>();

    public GSDailyInOut(){}

    public void add(GSDailyInOutGroup group)
    {
        this.list.add(group);
    }

    public GSDailyInOutGroup findByServiceType(String serviceType)
    {

        if (this.list.size() == 0)
            return null;

        for(GSDailyInOutGroup diog : this.list)
        {
            if (diog.serviceType.equals(serviceType))
                return diog;
        }

        return null;

    }

    public void print()
    {

        Log.d(AppConfig.TAG, "DEBUGGING : " + this.getClass().getName() + " : Size of list : " + this.list.size());

        for(GSDailyInOutGroup group : this.list)
        {
            group.print();
        }

    }

}
