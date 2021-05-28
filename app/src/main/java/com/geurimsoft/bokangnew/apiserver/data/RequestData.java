/**
 * API 질의용 클래스
 *
 * 2021. 05. 28 최초 생성
 *
 *  Writtend by jcm5758
 */

package com.geurimsoft.bokangnew.apiserver.data;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class RequestData
{

    @SerializedName("GSType")
    private String gsType;

    @SerializedName("GSQuery")
    private HashMap<String, String> gsQuery;

    /**
     * 생성자
     * @param gsType API 구별 Type
     * @param gsQuery request 에 들어가는 DATA
     */
    public RequestData(String gsType, HashMap<String, String> gsQuery)
    {
        this.gsType = gsType;
        this.gsQuery = gsQuery;
    }

    public String getGsType()
    {
        return gsType;
    }

    public void setGsType(String gsType)
    {
        this.gsType = gsType;
    }

    public HashMap<String, String> getGsQuery()
    {
        return gsQuery;
    }

    public void setGsQuery(HashMap<String, String> gsQuery)
    {
        this.gsQuery = gsQuery;
    }

}