package com.talebase.cloud.base.ms.admin.dto;

import java.util.List;

/**
 * Created by xia.li on 2016-12-7.
 */
public class DPer {

    private Integer type;
    private List<DPermissionOfRole> permissions;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<DPermissionOfRole> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<DPermissionOfRole> permissions) {
        this.permissions = permissions;
    }
}
