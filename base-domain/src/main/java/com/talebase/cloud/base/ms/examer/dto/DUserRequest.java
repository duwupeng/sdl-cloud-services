package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by zhangchunlin on 2016-12-14.
 */
public class DUserRequest {

    /**
     * 公司ID
     */
    Integer companyId;

    /**
     * 用户账号
     */
    String account;

    /**
     * 用户密码
     */
    String password;

    /**
     * 手机号码
     */
    String mobile;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
