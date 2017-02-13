package com.talebase.cloud.base.ms.notify.dto;

import com.talebase.cloud.common.util.StringUtil;

/**
 * Created by bin.yang on 2016-12-6.
 */
public class DPageSearchData {

    public static String SEARCH_ACCOUNT = "account";
    public static String SEARCH_EMAIL = "email";
    public static String SEARCH_NAME = "name";
    public static String SEARCH_MOBILE = "mobile";

    private String ids;
    private String searchType;//查询条件
    private String key;//搜索内容
    private Integer projectId;
    private Integer taskId;
    private Integer roleId;//角色id
    private Integer sendStatus;//发送状态
    private String sendDateBegin;
    private String sendDateEnd;
    private Integer sendType;//发送类型

    public boolean isSearchByAccount() {
        if (SEARCH_ACCOUNT.equals(searchType) && !StringUtil.isEmpty(key)) {
            return true;
        }
        return false;
    }

    public boolean isSearchByEmail() {
        if (SEARCH_EMAIL.equals(searchType) && !StringUtil.isEmpty(key)) {
            return true;
        }
        return false;
    }

    public boolean isSearchByName() {
        if (SEARCH_NAME.equals(searchType) && !StringUtil.isEmpty(key)) {
            return true;
        }
        return false;
    }

    public boolean isSearchByMobile() {
        if (SEARCH_MOBILE.equals(searchType) && !StringUtil.isEmpty(key)) {
            return true;
        }
        return false;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSendDateBegin() {
        return sendDateBegin;
    }

    public void setSendDateBegin(String sendDateBegin) {
        this.sendDateBegin = sendDateBegin;
    }

    public String getSendDateEnd() {
        return sendDateEnd;
    }

    public void setSendDateEnd(String sendDateEnd) {
        this.sendDateEnd = sendDateEnd;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }
}
