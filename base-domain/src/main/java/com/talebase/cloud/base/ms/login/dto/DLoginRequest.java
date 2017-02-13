package com.talebase.cloud.base.ms.login.dto;

import com.talebase.cloud.base.ms.login.enumes.LoginTypeEnume;

/**
 * Created by zhangchunlin on 2016-12-14.
 */
public class DLoginRequest {
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
     * 验证码
     */
    String verificationCode;

    /**
     * 登录类型（0：考生；1：管理员）
     */
    Integer loginType;

    /**
     * 调用源
     *
     *
     */
    public Integer callerFrom; // optional
    /**
     * 调用源IP
     *
     */
    public String callerIP; // optional
    /**
     * 手机号码
     *
     */
    public String mobile; // optional

    public Integer projectId; // optional

    public Integer taskId; // optional

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

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

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Integer getCallerFrom() {
        return callerFrom;
    }

    public void setCallerFrom(Integer callerFrom) {
        this.callerFrom = callerFrom;
    }

    public String getCallerIP() {
        return callerIP;
    }

    public void setCallerIP(String callerIP) {
        this.callerIP = callerIP;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
