package com.talebase.cloud.base.ms.admin.domain;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */public class TAdmin {
    /**
     * 用户id
     */
    Integer id;

    /**
     * 用户账号
     */
    String account;

    /**
     * 用户密码
     */
    String password;

    /**
     * 用户名称
     */
    String name;

    /**
     * 用户邮件
     */
    String email;

    /**
     * 用户状态,0:无效(invalid);1:有效(effective);-1:删除(deleted)

     */
    Integer status;

    /**
     * 创建日期
     */
    java.sql.Timestamp createdDate;

    /**
     * 创建人
     */
    String creater;

    /**
     * 用户信息修改时间
     */
    java.sql.Timestamp modifiedDate;

    /**
     * 用户信息修改人
     */
    String modifier;

    /**
     * 用户权限
     */
    String orgCode;

    /**
     * 企业id
     */
    Integer companyId;
    /**
     * 电话号码
     */
    String mobile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
