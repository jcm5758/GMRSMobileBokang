/**
 * 사용자 정보 저장 객체
 *
 * 2021. 05. 28 최초 생성
 *
 *  Writtend by jcm5758
 *
 */

package com.geurimsoft.bokangnew.apiserver.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserInfoData implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("userID")
    private String userID;

    @SerializedName("name")
    private String name;

    public UserInfoData(int id, String userID, String name)
    {
        this.id = id;
        this.userID = userID;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}