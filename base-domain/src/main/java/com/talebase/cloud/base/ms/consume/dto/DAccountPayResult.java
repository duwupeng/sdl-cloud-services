package com.talebase.cloud.base.ms.consume.dto;

import java.util.Date;

/**
 * 查询充值记录结果
 * Created by suntree.xu on 2016-12-7.
 */

public class DAccountPayResult {

    private String account;
    private int pointVar;
    private int smsVar;
    private Date modifiedDate;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getPointVar() {
        return pointVar;
    }

    public void setPointVar(int pointVar) {
        this.pointVar = pointVar;
    }

    public int getSmsVar() {
        return smsVar;
    }

    public void setSmsVar(int smsVar) {
        this.smsVar = smsVar;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
