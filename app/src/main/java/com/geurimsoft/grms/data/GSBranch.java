package com.geurimsoft.grms.data;

public class GSBranch
{

    private int branchID;
    private int branchType;
    private String branchName;
    private int dbName;
    private int[] auth_day;
    private int[] auth_month;
    private int[] auth_year;
    private int addMode_type;

    public GSBranch() { }

    public int getBranchID()
    {
        return branchID;
    }

    public void setBranchID(int branchID)
    {
        this.branchID = branchID;
    }

    public int getBranchType()
    {
        return branchType;
    }

    public void setBranchType(int branchType)
    {
        this.branchType = branchType;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public int getDbName()
    {
        return dbName;
    }

    public void setDbName(int dbName)
    {
        this.dbName = dbName;
    }

    public int[] getAuth_day()
    {
        return auth_day;
    }

    public void setAuth_day(int[] auth_day)
    {
        this.auth_day = auth_day;
    }

    public int[] getAuth_month()
    {
        return auth_month;
    }

    public void setAuth_month(int[] auth_month)
    {
        this.auth_month = auth_month;
    }

    public int[] getAuth_year()
    {
        return auth_year;
    }

    public void setAuth_year(int[] auth_year)
    {
        this.auth_year = auth_year;
    }

    public int getAddMode_type()
    {
        return addMode_type;
    }

    public void setAddMode_type(int addMode_type)
    {
        this.addMode_type = addMode_type;
    }

}
