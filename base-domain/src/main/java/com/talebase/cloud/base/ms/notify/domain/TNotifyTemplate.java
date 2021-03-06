package com.talebase.cloud.base.ms.notify.domain;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TNotifyTemplate {
    /**
     * 模板编号
     */
    Integer id;

    /**
     * 企业编号
     */
    Integer companyId;

    /**
     * 模板名称
     */
    String name;

    /**
     * 模板内容
     */
    String content;

    /**
     * 邮件主题
     */
    String subject;

    /**
     * 邮件签名
     */
    String sign;

    /**
     * 类型,0:系统(system);1:自定义(customize)
     */
    Integer type;

    /**
     * 发送方法,0:邮件(mail);1:短信(sms);2:微信(wechat)
     */
    Integer method;

    /**
     * 状态,0:禁用(disable);1:启用(enable);2:删除(delete)
     */
    Integer status;

    /**
     * 是否默认,0:否(no);1:是(yes)
     */
    Integer whetherDefault;

    /**
     * 创建人
     */
    String creator;

    /**
     * 创建时间
     */
    java.sql.Timestamp createdDate;

    /**
     * 修改人
     */
    String modifier;

    /**
     * 修改时间
     */
    java.sql.Timestamp modifiedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWhetherDefault() {
        return whetherDefault;
    }

    public void setWhetherDefault(Integer whetherDefault) {
        this.whetherDefault = whetherDefault;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
