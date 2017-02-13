package com.talebase.cloud.base.ms.admin.domain;

/**
 * Created by kanghongzhao on 16/11/27.
 */
public class TRolePermission {

    private Integer id;
    private Integer roleId;
    private Integer permissionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
}
