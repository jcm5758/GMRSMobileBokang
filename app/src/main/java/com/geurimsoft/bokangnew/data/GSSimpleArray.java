package com.geurimsoft.bokangnew.data;

import java.util.ArrayList;

public class GSSimpleArray
{

    public String Status;

    public String Message;

    public String[] List;

    public GSSimpleArray(String status, String message, String[] data)
    {
        this.Status = status;
        this.Message = message;
        this.List = data;
    }

    public ArrayList getArrayList()
    {

        if (this.List == null) return null;

        ArrayList tempList = new ArrayList();

        for(String s : this.List)
        {
            tempList.add(s);
        }

        return tempList;

    }


}
