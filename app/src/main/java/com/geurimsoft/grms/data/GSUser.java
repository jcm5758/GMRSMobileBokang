package com.geurimsoft.grms.data;

import java.util.List;

public class GSUser extends GSData
{

    private String userID = "";
    private String pwd = "";
    private int department = 0;
    private String apiKey = "";
    private int charge1 = -1;
    private int charge2 = -1;
    private int charge3 = -1;
    private int[] chargeSequence = new int[] { -1, -1 };
    private List<GSUserRight> userRightList;
    private List<GSBranch> branchList;
    private GSUserRight userRight;
    private boolean isAdmin = false;

    public GSUser()
    {
        super(-1, null);
    }

    public GSUser(String userID, String name, String pwd, int department, int charge1, int charge2, int charge3, String apiKey)
    {

        super(-1, name);

        this.userID = userID;
        this.pwd = pwd;
        this.department = department;
        this.charge1 = charge1;
        this.charge2 = charge2;
        this.charge3 = charge3;
        this.apiKey = apiKey;

    }

    public GSUser(int id, String userID, String name, String pwd, int department, int charge1, int charge2, int charge3, String apiKey)
    {

        super(id, name);

        this.userID = userID;
        this.pwd = pwd;
        this.department = department;
        this.charge1 = charge1;
        this.charge2 = charge2;
        this.charge3 = charge3;
        this.apiKey = apiKey;

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getCharge1() {
        return charge1;
    }

    public void setCharge1(int charge1) {
        this.charge1 = charge1;
    }

    public int getCharge2() {
        return charge2;
    }

    public void setCharge2(int charge2) {
        this.charge2 = charge2;
    }

    public int getCharge3() {
        return charge3;
    }

    public void setCharge3(int charge3) {
        this.charge3 = charge3;
    }

    public int[] getChargeSequence() {
        return chargeSequence;
    }

    public void setChargeSequence(int[] chargeSequence) {
        this.chargeSequence = chargeSequence;
    }

    public List<GSUserRight> getUserRightList() {
        return userRightList;
    }

    public void setUserRightList(List<GSUserRight> userRightList) {
        this.userRightList = userRightList;
    }

    public GSUserRight getUserRight() {
        return userRight;
    }

    public void setUserRight(GSUserRight userRight) {
        this.userRight = userRight;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<GSBranch> getBranchList() {  return branchList; }
    public void setBranchList(List<GSBranch> branchList) { this.branchList = branchList; }

}
