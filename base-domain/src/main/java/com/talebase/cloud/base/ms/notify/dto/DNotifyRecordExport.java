package com.talebase.cloud.base.ms.notify.dto;

/**
 * Created by bin.yang on 2016-11-28.
 */
public class DNotifyRecordExport {

    /**
     * 发送主题
     */
    String sendSubject;

    /**
     * 发送内容
     */
    String sendContent;

    /**
     * 项目名称
     */
    String projectName;

    /**
     * 产品名称
     */
    String taskName;


    /**
     * 帐号
     */
    String account;

    /**
     * 密码
     */
    String password;

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
    String sendDate;

    /**
     * 发送状态,0:发送失败(send_fail);1:发送成功(send_success);2:待发送(to_be_send);3:发送中(sending)
     */
    String sendStatus;

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }
}
