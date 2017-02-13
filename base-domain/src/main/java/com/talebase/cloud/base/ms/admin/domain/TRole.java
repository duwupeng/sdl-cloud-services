package com.talebase.cloud.base.ms.admin.domain;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */public class TRole {
    /**
     * 角色id
     */
    Integer id;

    /**
     * 角色名称
     */
    String name;

    /**
     * 角色状态,0:无效(invalid);1:有效(effective);-1:删除(deleted)
     */
    Integer status;

    /**
     * 角色创建时间
     */
    java.sql.Timestamp createdDate;

    /**
     * 角色创建人
     */
    String creater;

    /**
     * 角色修改时间
     */
    java.sql.Timestamp modifiedDate;

    /**
     * 角色修改人
     */
    String modifier;

    /**
     * 企业id
     */
    Integer companyId;

    /**
     * 角色类型
     */
    Integer roleType;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
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
