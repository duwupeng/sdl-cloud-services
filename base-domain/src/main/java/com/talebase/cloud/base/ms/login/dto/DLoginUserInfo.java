package com.talebase.cloud.base.ms.login.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchunlin on 2016-12-14.
 */
public class DLoginUserInfo {
   /**
    * ID
    */
    Integer id;
   /**
    * 公司ID
    */
    Integer companyId;

    /**
     * 用户账号
     */
    String account;

    /**
     *
     */
    String token;

    /**
     * 姓名
     */
    String name;

    /**
     * 角色名称
     */
//    String roleName;

    /**
     * 分组编码
     */
    String orgCode;

    String tips;

    /**
     * 权限码集合
     */
    List<String> permissionCodes = new ArrayList<>();

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public String getRoleName() {
//        return roleName;
//    }
//
//    public void setRoleName(String roleName) {
//        this.roleName = roleName;
//    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public List<String> getPermissionCodes() {
        return permissionCodes;
    }

    public void setPermissionCodes(List<String> permissionCodes) {
        this.permissionCodes = permissionCodes;
    }
}
