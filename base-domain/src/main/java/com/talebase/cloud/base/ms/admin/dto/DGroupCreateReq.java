package com.talebase.cloud.base.ms.admin.dto;

/**
 * Created by kanghong.zhao on 2016-11-24.
 */
public class DGroupCreateReq {

    private String name;
    private Integer parentId;
    private Integer companyId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
