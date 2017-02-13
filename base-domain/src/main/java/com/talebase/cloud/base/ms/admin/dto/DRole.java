package com.talebase.cloud.base.ms.admin.dto;

/**
 * Created by kanghongzhao on 16/11/26.
 */
public class DRole {

    private Integer id;
    private Integer companyId;
    private String name;
    private Integer memberNum;
    private Integer roleType;

    private String operatePermission = "0";

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

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public String getOperatePermission() {
        return operatePermission;
    }

    public void setOperatePermission(String operatePermission) {
        this.operatePermission = operatePermission;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }
}
