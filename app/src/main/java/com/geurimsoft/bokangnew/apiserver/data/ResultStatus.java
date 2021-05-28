/**
 * API 서버 응답 객체
 * 일반적인 형식 
 * 구체적인 내용에 따라 재정의
 *
 * 2021. 05. 28 최초 생성
 *
 *  Writtend by jcm5758
 *
 */
package com.geurimsoft.bokangnew.apiserver.data;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResultStatus
{

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList data;

    public ResultStatus(String status, String message, ArrayList data)
    {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

}