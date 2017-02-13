package com.talebase.cloud.base.ms.admin.dto;

import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.base.ms.admin.domain.TRole;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-11-30.
 */
public class DGroupAndRole {

    private List<TGroup> groupList;

    private List<TRole> roleList;

    public List<TGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<TGroup> groupList) {
        this.groupList = groupList;
    }

    public List<TRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<TRole> roleList) {
        this.roleList = roleList;
    }
}
