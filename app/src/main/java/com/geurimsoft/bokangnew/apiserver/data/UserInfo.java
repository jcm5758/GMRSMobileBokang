/**
 * 사용자 정보
 * 로그인시 사용자 정보와 권한 정보를 저장
 *
 * 2021. 05. 28 최초 생성
 *
 *  Writtend by jcm5758
 *
 */

package com.geurimsoft.bokangnew.apiserver.data;

import android.util.Log;

import com.geurimsoft.bokangnew.data.GSConfig;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfo implements Serializable
{

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("userinfo")
    private ArrayList<UserInfoData> userinfo;

    @SerializedName("userright")
    private ArrayList<UserRightData> userright;

    public UserInfo(String status, String message, ArrayList<UserInfoData> userinfo, ArrayList<UserRightData> userright)
    {

        this.status = status;
        this.message = message;
        this.userinfo = userinfo;
        this.userright = userright;

    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserinfo(ArrayList<UserInfoData> userinfo) {
        this.userinfo = userinfo;
    }

    public ArrayList<UserInfoData> getUserinfo() {
        return userinfo;
    }

    public boolean isUserInfoNull()
    {

        if (this.userinfo == null || this.userinfo.size() == 0)
            return true;
        else
            return false;

    }

    public UserRightData getUserRightData(int ind)
    {
        Log.d(GSConfig.APP_DEBUG, "ind : " + ind);
        Log.d(GSConfig.APP_DEBUG, "size : " + this.userright.size());
        return userright.get(ind);
    }
    
    public ArrayList<UserRightData> getUserright() {
        return userright;
    }

    public void setUserright(ArrayList<UserRightData> userright) {
        this.userright = userright;
    }

    public boolean isUserRightNull()
    {

        if (this.userright == null || this.userright.size() == 0)
            return true;
        else
            return false;

    }

}
