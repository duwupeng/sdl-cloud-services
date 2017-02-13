package com.talebase.cloud.base.ms.admin.dto;


/**
 * Created by daorong.li on 2016-11-25.
 * 分页数据
 */
public class DPageSearchData {

    public static String SEARCH_ACCOUNT ="account";
    public static String SEARCH_EMAIL ="email";
    public static String SEARCH_NAME ="name";
    public static String SEARCH_MOBLIE ="mobile";

    private String searchType;//查询条件
    private String key;//搜索内容
    private Integer groupId ;//分组id
    private Integer roleId ;//角色id
    private Integer status ;//状态
    private DGroupAndRole dGroupAndRole;

    public boolean isSearchByAccount(){
        if (SEARCH_ACCOUNT.equals(searchType)
                &&(key != "" && !"".equals(key))){
            return true;
        }
        return false;
    }

    public boolean isSearchByEmail(){
        if (SEARCH_EMAIL.equals(searchType)
                &&(key != "" && !"".equals(key))){
            return  true;
        }
        return false;
    }

    public boolean isSearchByName(){
        if (SEARCH_NAME.equals(searchType)
                &&(key != "" && !"".equals(key))){
            return  true;
        }
        return false;
    }

    public boolean isSearchByMoblie(){
        if (SEARCH_MOBLIE.equals(searchType)
                &&(key != "" && !"".equals(key))){
            return  true;
        }
        return false;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public DGroupAndRole getdGroupAndRole() {
        return dGroupAndRole;
    }

    public void setdGroupAndRole(DGroupAndRole dGroupAndRole) {
        this.dGroupAndRole = dGroupAndRole;
    }

}
