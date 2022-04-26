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

public class UserRightData implements Serializable
{

    // 로그인-로그인-모바일 로그인
    // public static int USER_RIGHT_LOGIN_LOGIN_MOBILE = 65;
    @SerializedName("ur01")
    private int ur01;

    // 모바일-입고 현황-입고현황 조회
    // public static int USER_RIGHT_MOBILE_INPUT_LIST = 66;
    @SerializedName("ur02")
    private int ur02;

    // 모바일-입고 현황-입고현황 조회(가격포함)
    // public static int USER_RIGHT_MOBILE_INPUT_LIST_PRICE = 67;
    @SerializedName("ur03")
    private int ur03;

    // 모바일-입고 현황-입고현황 승인
    // public static int USER_RIGHT_MOBILE_INPUT_AUTH = 68;
    @SerializedName("ur04")
    private int ur04;

    // 모바일-입고 현황-출고현황 조회
    // public static int USER_RIGHT_MOBILE_OUTPUT_LIST = 69;
    @SerializedName("ur05")
    private int ur05;

    // 모바일-입고 현황-출고현황 조회(가격포함)
    // public static int USER_RIGHT_MOBILE_OUTPUT_LIST_PRICE = 70;
    @SerializedName("ur06")
    private int ur06;

    // 모바일-입고 현황-출고현황 승인
    // public static int USER_RIGHT_MOBILE_OUTPUT_AUTH = 71;
    @SerializedName("ur07")
    private int ur07;

    // 모바일-입고 현황-매출현황 조회
    // public static int USER_RIGHT_MOBILE_SALES_LIST = 72;
    @SerializedName("ur08")
    private int ur08;

    // 모바일-입고 현황-매출현황 승인
    // public static int USER_RIGHT_MOBILE_SALES_AUTH = 73
    @SerializedName("ur09")
    private int ur09;

    // 모바일-입고 현황-매입현황 조회
    // public static int USER_RIGHT_MOBILE_BUYS_LIST = 84;
    @SerializedName("ur10")
    private int ur10;

    // 모바일-입고 현황-매입현황 승인
    // public static int USER_RIGHT_MOBILE_BUYS_AUTH = 85;
    @SerializedName("ur11")
    private int ur11;

    // 모바일-월보 입고-조회
    // public static int USER_RIGHT_MOBILE_MONTH_INPUT_LIST = 86;
    @SerializedName("ur12")
    private int ur12;

    // 모바일-월보 입고-조회 가격
    // public static int USER_RIGHT_MOBILE_MONTH_INPUT_LIST_PRICE = 87;
    @SerializedName("ur13")
    private int ur13;

    // 모바일-월보 출고-조회
    // public static int USER_RIGHT_MOBILE_MONTH_OUTPUT_LIST = 88;
    @SerializedName("ur14")
    private int ur14;

    // 모바일-월보 출고-조회 가격
    // public static int USER_RIGHT_MOBILE_MONTH_OUTPUT_LIST_PRICE = 89;
    @SerializedName("ur15")
    private int ur15;

    // 모바일-월보 일별-조회
    // public static int USER_RIGHT_MOBILE_MONTH_DAY_LIST = 90;
    @SerializedName("ur16")
    private int ur16;

    // 모바일-연보 월별-조회
    // public static int USER_RIGHT_MOBILE_YEAR_MONTH_LIST = 91;
    @SerializedName("ur17")
    private int ur17;

    // 모바일 단가 조회
    // 102
    @SerializedName("ur18")
    private int ur18;

    @SerializedName("branID")
    private int branID;

    @SerializedName("branName")
    private String branName;

    @SerializedName("branShortName")
    private String branShortName;

    public UserRightData(int ur01, int ur02, int ur03, int ur04, int ur05, int ur06,
                         int ur07, int ur08, int ur09, int ur10, int ur11, int ur12,
                         int ur13, int ur14, int ur15, int ur16, int ur17, int ur18,
                         int branID, String branName, String branShortName)
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
        this.ur18 = ur18;
        this.branID = branID;
        this.branName = branName;
        this.branShortName = branShortName;

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

    public int getUr18() {
        return ur18;
    }

    public void setUr18(int ur18) {
        this.ur18 = ur18;
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

    public String getBranShortName() {
        return branShortName;
    }

    public boolean isShowDayAmount()
    {

        if ( this.ur02 == 1 && this.ur05 == 1 )
            return true;

        if ( this.ur03 == 1 && this.ur06 == 1 )
            return true;

        return false;

    }

    public boolean isShowDayPrice()
    {

        if ( this.ur03 == 1 && this.ur06 == 1 )
            return true;

        return false;

    }

    public boolean isShowDaySearch()
    {

        if ( this.ur18 == 1)
            return true;

        return false;

    }

}
