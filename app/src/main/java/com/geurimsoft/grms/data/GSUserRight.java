package com.geurimsoft.grms.data;

import com.geurimsoft.conf.AppConfig;

public class GSUserRight extends GSData
{

    private int userID;
    private int branchID;
    private int dbName = 0;
    private int branchType = 0;
    private int addMode_type = 0;
    private int[] auth_day;
    private int[] auth_month;
    private int[] auth_year;
    private int[] rightArray = new int[AppConfig.USER_RIGHT_SIZE];

    public GSUserRight()
    {

    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public int getBranchID()
    {
        return branchID;
    }

    public void setBranchID(int branchID)
    {
        this.branchID = branchID;
    }

    public int[] getRightArray()
    {
        return rightArray;
    }

    public void setRightArray(int[] rightArray)
    {
        this.rightArray = rightArray;
    }

    public int getDbName() {
        return dbName;
    }

    public void setDbName(int dbName) {
        this.dbName = dbName;
    }

    public int getBranchType() {
        return branchType;
    }

    public void setBranchType(int branchType) {
        this.branchType = branchType;
    }

    public int getAddMode_type() {
        return addMode_type;
    }

    public void setAddMode_type(int addMode_type) {
        this.addMode_type = addMode_type;
    }

    public int[] getAuth_day() {
        return auth_day;
    }

    public void setAuth_day(int[] auth_day) {
        this.auth_day = auth_day;
    }

    public int[] getAuth_month() {
        return auth_month;
    }

    public void setAuth_month(int[] auth_month) {
        this.auth_month = auth_month;
    }

    public int[] getAuth_year() {
        return auth_year;
    }

    public void setAuth_year(int[] auth_year) {
        this.auth_year = auth_year;
    }

}
