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

    @SerializedName("Status")
    private String Status;

    @SerializedName("Message")
    private String Message;

    @SerializedName("UserInfo")
    private ArrayList<UserInfoData> UserInfo;

    @SerializedName("UserRight")
    private ArrayList<UserRightData> UserRight;

    public UserInfo(String status, String message, ArrayList<UserInfoData> userinfo, ArrayList<UserRightData> userright)
    {

        this.Status = status;
        this.Message = message;
        this.UserInfo = userinfo;
        this.UserRight = userright;

    }

    public String getStatus()
    {
        return this.Status;
    }

    public void setStatus(String status)
    {
        this.Status = status;
    }

    public String getMessage() {
        return this.Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public void setUserinfo(ArrayList<UserInfoData> userinfo) {
        this.UserInfo = userinfo;
    }

    public ArrayList<UserInfoData> getUserinfo() {
        return this.UserInfo;
    }

    public boolean isUserInfoNull()
    {

        if (this.UserInfo == null || this.UserInfo.size() == 0)
            return true;
        else
            return false;

    }

    public UserRightData getUserRightData(int ind)
    {
        Log.d(GSConfig.APP_DEBUG, "ind : " + ind);
        Log.d(GSConfig.APP_DEBUG, "size : " + this.UserRight.size());
        return UserRight.get(ind);
    }
    
    public ArrayList<UserRightData> getUserright() {
        return UserRight;
    }

    public void setUserright(ArrayList<UserRightData> userright) {
        this.UserRight = userright;
    }

    public boolean isUserRightNull()
    {

        if (this.UserRight == null || this.UserRight.size() == 0)
            return true;
        else
            return false;

    }

}
