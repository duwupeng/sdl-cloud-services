package com.talebase.cloud.base.ms.email.dto;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */public class DUser {
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
     * 用户类型
     */
   int type;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
