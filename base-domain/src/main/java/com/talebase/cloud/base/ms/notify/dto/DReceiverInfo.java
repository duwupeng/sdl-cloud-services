package com.talebase.cloud.base.ms.notify.dto;

/**
 * Created by bin.yang on 2016-12-6.
 */
public class DReceiverInfo {

    /**
     * 编号
     */
    Integer id;
    /**
     * 姓名
     */
    String name;
    /**
     * 帐号
     */
    String account;
    /**
     * 密码
     */
    String password;
    /**
     * 手机
     */
    String mobile;
    /**
     * 邮箱
     */
    String email;
    /**
     * 性别
     */
    String gender;
    /**
     * 任务数量
     */
    Integer taskCount = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }
}
