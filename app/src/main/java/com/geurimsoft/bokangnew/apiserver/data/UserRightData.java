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

    // 로그인 					: 65 	ur01
    @SerializedName("ur01")
    private int ur01;

    // 일보 수량 조회 			: 66	ur02
    @SerializedName("ur02")
    private int ur02;

    // 일보 금액 조회 			: 67	ur03
    @SerializedName("ur03")
    private int ur03;

    // 단가 조회 				: 68	ur04
    @SerializedName("ur04")
    private int ur04;

    // 월보 수량 조회 			: 71	ur05
    @SerializedName("ur05")
    private int ur05;

    // 월보 금액 조회 			: 72	ur06
    @SerializedName("ur06")
    private int ur06;

    // 월보 수량(거래처별) 조회 : 73	ur07
    @SerializedName("ur07")
    private int ur07;

    // 월보 금액(거래처별) 조회 : 84	ur08
    @SerializedName("ur08")
    private int ur08;

    // 월보 그래프				: 85	ur09
    @SerializedName("ur09")
    private int ur09;

    // 연보 수량 조회 			: 86	ur10
    @SerializedName("ur10")
    private int ur10;

    // 연보 금액 조회 			: 87	ur11
    @SerializedName("ur11")
    private int ur11;

    // 연보 수량(거래처별) 조회 : 88	ur12
    @SerializedName("ur12")
    private int ur12;

    // 연보 금액(거래처별) 조회 : 89	ur13
    @SerializedName("ur13")
    private int ur13;

    // 연보 그래프 			: 90	ur14
    @SerializedName("ur14")
    private int ur14;

    @SerializedName("branID")
    private int branID;

    @SerializedName("branName")
    private String branName;

    @SerializedName("branShortName")
    private String branShortName;

    public UserRightData(int ur01, int ur02, int ur03, int ur04, int ur05,
                         int ur06, int ur07, int ur08, int ur09, int ur10,
                         int ur11, int ur12, int ur13, int ur14,
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

        if ( this.ur02 == 1)
            return true;

        return false;

    }

    public boolean isShowDayPrice()
    {

        if ( this.ur03 == 1 )
            return true;

        return false;

    }

    public boolean isShowDaySearch()
    {

        if ( this.ur04 == 1)
            return true;

        return false;

    }

    public boolean isShowMonthAmount()
    {

        if ( this.ur05 == 1 )
            return true;

        return false;

    }

    public boolean isShowMonthPrice()
    {

        if ( this.ur06 == 1 )
            return true;

        return false;

    }

    public boolean isShowMonthCustomerAmount()
    {

        if ( this.ur07 == 1 )
            return true;

        return false;

    }

    public boolean isShowMonthCustomerPrice()
    {

        if ( this.ur08 == 1 )
            return true;

        return false;

    }

    public boolean isShowMonthChart()
    {

        if ( this.ur09 == 1 )
            return true;

        return false;

    }

    public boolean isShowYearAmount()
    {

        if ( this.ur10 == 1 )
            return true;

        return false;

    }

    public boolean isShowYearPrice()
    {

        if ( this.ur11 == 1 )
            return true;

        return false;

    }

    public boolean isShowYearCustomerAmount()
    {

        if ( this.ur12 == 1 )
            return true;

        return false;

    }

    public boolean isShowYearCustomerPrice()
    {

        if ( this.ur13 == 1 )
            return true;

        return false;

    }

    public boolean isShowYearChart()
    {

        if ( this.ur14 == 1 )
            return true;

        return false;

    }

}
