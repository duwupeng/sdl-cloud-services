package com.talebase.cloud.base.ms.admin.dto;

import java.util.List;

/**
 * Created by kanghongzhao on 16/11/27.
 */
public class DRolePermissionUpdateReq {

    private Integer roleId;
    private List<Integer> permissionIds;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
