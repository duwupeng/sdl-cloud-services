package com.talebase.cloud.base.ms.admin.dto;

/**
 * Created by kanghongzhao on 16/11/26.
 */
public class DGroupNameModifyReq {

    private Integer groupId;
    private String newName;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
