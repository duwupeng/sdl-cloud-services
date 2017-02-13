package com.talebase.cloud.base.ms.admin.dto;

/**
 * @author auto-tool
 */public class DbzAdmin {

    /**
     * 用户账号
     */
    String bzAccount;

    /**
     * 用户密码
     */
    String bzPassword;

    /**
     * 用户名称
     */
    String bzName;

    /**
     * 用户邮件
     */
    String bzEmail;

    /**
     * 电话号码
     */
    String bzMobile;

    public String getBzAccount() {
        return bzAccount;
    }

    public void setBzAccount(String bzAccount) {
        this.bzAccount = bzAccount;
    }

    public String getBzPassword() {
        return bzPassword;
    }

    public void setBzPassword(String bzPassword) {
        this.bzPassword = bzPassword;
    }

    public String getBzName() {
        return bzName;
    }

    public void setBzName(String bzName) {
        this.bzName = bzName;
    }

    public String getBzEmail() {
        return bzEmail;
    }

    public void setBzEmail(String bzEmail) {
        this.bzEmail = bzEmail;
    }

    public String getBzMobile() {
        return bzMobile;
    }

    public void setBzMobile(String bzMobile) {
        this.bzMobile = bzMobile;
    }
}
