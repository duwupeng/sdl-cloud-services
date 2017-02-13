package com.talebase.cloud.base.ms.notify.domain;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TNotifyRecord {
    /**
     * 日志编号
     */
    Integer id;

    /**
     * 企业编号
     */
    Integer companyId;

    /**
     * 发送主题
     */
    String sendSubject;

    /**
     * 发送内容
     */
    String sendContent;

    /**
     * 发送类型,0:邮件(email);1:短信(sms);2:微信(wechat)
     */
    Integer sendType;

    /**
     * 角色编号
     */
    Integer roleId;

    /**
     * 项目编号
     */
    Integer projectId;

    /**
     * 项目名称
     */
    String projectName;

    /**
     * 产品编号
     */
    Integer taskId;

    /**
     * 产品名称
     */
    String taskName;

    /**
     * 帐号
     */
    String account;

    /**
     * 姓名
     */
    String name;

    /**
     * 邮箱
     */
    String email;

    /**
     * 手机
     */
    String mobile;

    /**
     * 发送人
     */
    String sender;

    /**
     * 发送时间
     */
    java.sql.Timestamp sendDate;

    /**
     * 发送状态,0:发送失败(send_fail);1:发送成功(send_success);2:待发送(to_be_send);3:发送中(sending)
     */
    Integer sendStatus;

    /**
     * 发送次数
     */
    Integer sendTimes;

    /**
     * 发送条数
     */
    Integer sendCount;

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

    public String getSendSubject() {
        return sendSubject;
    }

    public void setSendSubject(String sendSubject) {
        this.sendSubject = sendSubject;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(Integer sendTimes) {
        this.sendTimes = sendTimes;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }
}
