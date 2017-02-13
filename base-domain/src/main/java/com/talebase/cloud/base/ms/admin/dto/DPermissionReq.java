package com.talebase.cloud.base.ms.admin.dto;

import java.util.List;

/**
 * Created by zhangchunlin on 2017-1-12.
 */
public class DPermissionReq {

    private Integer id;
    private String name;
    private Integer companyId;
    private List<Integer> ids;
    private List<Integer> notIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getNotIds() {
        return notIds;
    }

    public void setNotIds(List<Integer> notIds) {
        this.notIds = notIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
