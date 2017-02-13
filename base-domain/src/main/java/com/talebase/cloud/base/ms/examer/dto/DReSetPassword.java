package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by daorong.li on 2016-12-12.
 */
public class DReSetPassword extends DUserExamPageRequest{

    private String ids;
    private boolean isRandom;
    private String newPassword;
    private Integer userId;
    private String account;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

