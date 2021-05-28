/**
 * 사용자 권한 정보 저장 객체
 *
 * 2021. 05. 28 최초 생성
 *
 *  Writtend by jcm5758
 *
 */

package com.geurimsoft.bokangnew.apiserver.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserRightData implements Serializable {

    @SerializedName("ur01")
    private int ur01;

    @SerializedName("ur02")
    private int ur02;

    @SerializedName("ur03")
    private int ur03;

    @SerializedName("ur04")
    private int ur04;

    @SerializedName("ur05")
    private int ur05;

    @SerializedName("ur06")
    private int ur06;

    @SerializedName("ur07")
    private int ur07;

    @SerializedName("ur08")
    private int ur08;

    @SerializedName("ur09")
    private int ur09;

    @SerializedName("ur10")
    private int ur10;

    @SerializedName("ur11")
    private int ur11;

    @SerializedName("ur12")
    private int ur12;

    @SerializedName("ur13")
    private int ur13;

    @SerializedName("ur14")
    private int ur14;

    @SerializedName("ur15")
    private int ur15;

    @SerializedName("ur16")
    private int ur16;

    @SerializedName("ur17")
    private int ur17;

    @SerializedName("branID")
    private int branID;

    @SerializedName("branName")
    private String branName;

    public UserRightData(int ur01, int ur02, int ur03, int ur04, int ur05, int ur06,
                         int ur07, int ur08, int ur09, int ur10, int ur11, int ur12,
                         int ur13, int ur14, int ur15, int ur16, int ur17, int branID, String branName)
    {

        this.ur01 = ur01;
        this.ur02 = ur02;
        this.ur03 = ur03;
        this.ur04 = ur04;
        this.ur05 = ur05;
        this.ur06 = ur06;
        this.ur07 = ur07;
        this.ur08 = ur08;
        this.ur09 = ur09;
        this.ur10 = ur10;
        this.ur11 = ur11;
        this.ur12 = ur12;
        this.ur13 = ur13;
        this.ur14 = ur14;
        this.ur15 = ur15;
        this.ur16 = ur16;
        this.ur17 = ur17;
        this.branID = branID;
        this.branName = branName;

    }

    public int getUr01() {
        return ur01;
    }

    public void setUr01(int ur01) {
        this.ur01 = ur01;
    }

    public int getUr02() {
        return ur02;
    }

    public void setUr02(int ur02) {
        this.ur02 = ur02;
    }

    public int getUr03() {
        return ur03;
    }

    public void setUr03(int ur03) {
        this.ur03 = ur03;
    }

    public int getUr04() {
        return ur04;
    }

    public void setUr04(int ur04) {
        this.ur04 = ur04;
    }

    public int getUr05() {
        return ur05;
    }

    public void setUr05(int ur05) {
        this.ur05 = ur05;
    }

    public int getUr06() {
        return ur06;
    }

    public void setUr06(int ur06) {
        this.ur06 = ur06;
    }

    public int getUr07() {
        return ur07;
    }

    public void setUr07(int ur07) {
        this.ur07 = ur07;
    }

    public int getUr08() {
        return ur08;
    }

    public void setUr08(int ur08) {
        this.ur08 = ur08;
    }

    public int getUr09() {
        return ur09;
    }

    public void setUr09(int ur09) {
        this.ur09 = ur09;
    }

    public int getUr10() {
        return ur10;
    }

    public void setUr10(int ur10) {
        this.ur10 = ur10;
    }

    public int getUr11() {
        return ur11;
    }

    public void setUr11(int ur11) {
        this.ur11 = ur11;
    }

    public int getUr12() {
        return ur12;
    }

    public void setUr12(int ur12) {
        this.ur12 = ur12;
    }

    public int getUr13() {
        return ur13;
    }

    public void setUr13(int ur13) {
        this.ur13 = ur13;
    }

    public int getUr14() {
        return ur14;
    }

    public void setUr14(int ur14) {
        this.ur14 = ur14;
    }

    public int getUr15() {
        return ur15;
    }

    public void setUr15(int ur15) {
        this.ur15 = ur15;
    }

    public int getUr16() {
        return ur16;
    }

    public void setUr16(int ur16) {
        this.ur16 = ur16;
    }

    public int getUr17() {
        return ur17;
    }

    public void setUr17(int ur17) {
        this.ur17 = ur17;
    }

    public int getBranID() {
        return branID;
    }

    public void setBranID(int branID) {
        this.branID = branID;
    }

    public String getBranName() {
        return branName;
    }

    public void setBranName(String branName) {
        this.branName = branName;
    }

}
