package com.talebase.cloud.base.ms.admin.dto;

/**
 * Created by kanghongzhao on 16/11/26.
 */
public class DRoleNameModifyReq {

    private Integer roleId;
    private String newName;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
