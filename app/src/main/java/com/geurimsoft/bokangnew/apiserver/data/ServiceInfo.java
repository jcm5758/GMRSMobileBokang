package com.geurimsoft.bokangnew.apiserver.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceInfo implements Serializable
{

    @SerializedName("status")
    private String status;

    @SerializedName("inputHeader")
    private ArrayList<UserInfoData> inputHeader;

    @SerializedName("userright")
    private ArrayList<UserRightData> userright;

    public ServiceInfo(String status, String message, ArrayList<UserInfoData> userinfo, ArrayList<UserRightData> userright)
    {

        this.status = status;


    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }



}