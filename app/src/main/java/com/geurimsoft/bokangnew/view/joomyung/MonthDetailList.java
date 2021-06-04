package com.geurimsoft.bokangnew.view.joomyung;

import com.geurimsoft.bokangnew.view.etc.CustomProgressDialog;

public class MonthDetailList {

    private String queryDate;
    private String responseMessage;

    // 거래처 full name
    private String customerName;

    // 지점 ID
    private int branchID;

    // Unit / Money
    private int statsType;

    // 입고 / 출고 / 토사
    private int serviceType;

    private CustomProgressDialog progressDialog;

    public MonthDetailList(String customerName, int branchID, int statsType, int serviceType, String _queryDate)
    {
        this.queryDate = _queryDate;
        this.customerName = customerName;
        this.branchID = branchID;
        this.statsType = statsType;
        this.serviceType = serviceType;
    }

}
