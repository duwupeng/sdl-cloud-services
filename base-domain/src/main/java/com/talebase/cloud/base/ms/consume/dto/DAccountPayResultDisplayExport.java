package com.talebase.cloud.base.ms.consume.dto;

import java.util.Date;

/**
 * Created by suntree.xu on 2016-12-13.
 */
public class DAccountPayResultDisplayExport {

    private String account;
    private String type;
    private int val;
    private String modifiedDate;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
