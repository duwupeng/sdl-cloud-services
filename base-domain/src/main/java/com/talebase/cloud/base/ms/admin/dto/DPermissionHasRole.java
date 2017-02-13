package com.talebase.cloud.base.ms.admin.dto;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
public class DPermissionHasRole {

    private Integer id;
    private String name;
    private Integer type;
    private boolean roleHasRight = false;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isRoleHasRight() {
        return roleHasRight;
    }

    public void setRoleHasRight(boolean roleHasRight) {
        this.roleHasRight = roleHasRight;
    }
}
