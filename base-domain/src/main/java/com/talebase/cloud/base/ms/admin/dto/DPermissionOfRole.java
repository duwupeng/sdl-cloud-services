package com.talebase.cloud.base.ms.admin.dto;

import java.util.List;

/**
 * Created by kanghongzhao on 16/11/27.
 */
public class DPermissionOfRole {

    private Integer type;
    private Integer id;
    private String name;
    private Boolean hasPermission = false;

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

    public Boolean getHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(Boolean hasPermission) {
        this.hasPermission = hasPermission;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
