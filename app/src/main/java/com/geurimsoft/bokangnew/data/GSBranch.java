package com.geurimsoft.bokangnew.data;

public class GSBranch
{

    private int branchID;
    private String branchName;
    private String branchShortName;

    public GSBranch(int branchID, String branchName, String branchShortName)
    {
        this.branchID = branchID;
        this.branchName = branchName;
        this.branchShortName = branchShortName;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchShortName() {
        return branchShortName;
    }

    public void setBranchShortName(String branchShortName) {
        this.branchShortName = branchShortName;
    }

}
