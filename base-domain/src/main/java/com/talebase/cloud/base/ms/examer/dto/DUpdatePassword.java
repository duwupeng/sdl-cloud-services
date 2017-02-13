package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by zhangchunlin on 2017-1-13.
 */
public class DUpdatePassword {

    private String oldPassword;
    private String newPassword;
    private Integer userId;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

}

