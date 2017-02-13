package com.talebase.cloud.base.ms.admin.dto;

import com.talebase.cloud.base.ms.admin.enums.TAdminStatus;
import com.talebase.cloud.common.util.Des3Util;

import java.sql.Timestamp;

/**
 * Created by daorong.li on 2016-11-28.
 * 管理员导入
 */
public class DUploadFileData {

    //private static int INVALID = 0;//禁用
    //private static int EFFECTIVE = 1;//启用

    private String account;
    private String password;
    private String name;
    private String email;
    private String roleName;
    private String groupName;
    private int    status;
    private String statusName;
    private String mobile;
    private String creater;
    private String createdDate;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        try{
            return Des3Util.des3DecodeCBC(password);
        }catch (Exception e){}
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        if (status == TAdminStatus.INVALID.getValue())
            return "禁用";
        else if (status == TAdminStatus.EFFECTIVE.getValue())
            return "启用";
        return "";
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
