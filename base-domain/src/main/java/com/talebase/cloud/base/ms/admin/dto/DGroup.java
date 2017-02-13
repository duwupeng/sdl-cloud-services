package com.talebase.cloud.base.ms.admin.dto;

/**
 * Created by kanghong.zhao on 2016-11-24.
 */
public class DGroup {

    private Integer id;
    private String name;
    private int memberNum;
    private String orgCode;

    private String operatePermission = "0";
    private Boolean hasSon = false;

//    private Boolean hasQueryMemberRight = false;
//    private Boolean hasAddTeamRight = false;
//    private Boolean hasModifyRight = false;
//    private Boolean hasDeleteRight = false;

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

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOperatePermission() {
        return operatePermission;
    }

    public void setOperatePermission(String operatePermission) {
        this.operatePermission = operatePermission;
    }

    public Boolean getHasSon() {
        return hasSon;
    }

    public void setHasSon(Boolean hasSon) {
        this.hasSon = hasSon;
    }
}
