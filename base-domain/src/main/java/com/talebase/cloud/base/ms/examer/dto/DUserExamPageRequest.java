package com.talebase.cloud.base.ms.examer.dto;


import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.common.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by daorong.li on 2016-12-8.
 */
public class DUserExamPageRequest {

    private static String CREATETIME = "createTime";
    private static String BIRTHDAY = "birthday";
    private static String ACCOUNT1 = "account";
    private static String EMAIL = "email";
    private static String NAME = "name";
    private static String MOBILE = "mobile";
    public static String CMCWAY_EMAIL = "1";
    public static String CMCWAY_EMAIL_NO = "2";
    public static String CMCWAY_SMS = "3";
    public static String CMCWAY_SMS_NO = "4";
    public static String CMCSTATUS_EMAIL_SEND = "1";
    public static String CMCSTATUS_EMAIL_NO = "2";
    public static String CMCSTATUS_SMS = "3";
    public static String CMCSTATUS_SMS_NO = "4";
    public static Integer NOBEGIN = 1;
    public static Integer ANSWER = 2;
    public static Integer COMPLETE = 3;
    public static Integer SENDING = 1;
    public static Integer FAILURE = 2;
    public static Integer SUCCESS = 3;

    private String searchType1;
    private String searchType2;
    private String key1Begin;
    private String key1End;
    private String key2;
    private String cmctStatus;
    private String cmctWay;
    private String status;
    private Integer projectId;
    private Integer taskId;
    private Integer companyId;

    private String projectName;//项目管理名称
    private String taskName;//任务名称
    public boolean isCreateTime_begin() {
        if (CREATETIME.equals(searchType1)
                && !StringUtil.isEmpty(key1Begin)) {
            return true;
        }
        return false;
    }

    public boolean isCreateTime_end() {
        if (CREATETIME.equals(searchType1)
                && !StringUtil.isEmpty(key1End)) {
            return true;
        }
        return false;
    }

    public boolean isBirthday_begin() {
        if (BIRTHDAY.equals(searchType1)
                && !StringUtil.isEmpty(key1Begin)) {
            return true;
        }
        return false;
    }

    public boolean isBirthday_end() {
        if (BIRTHDAY.equals(searchType1)
                && !StringUtil.isEmpty(key1End)) {
            return true;
        }
        return false;
    }

    public boolean isAccount1() {
        if (ACCOUNT1.equals(searchType2)
                && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public boolean isEmail() {
        if (EMAIL.equals(searchType2)
                && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public boolean isName() {
        if (NAME.equals(searchType2)
                && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public boolean isMobile() {
        if (MOBILE.equals(searchType2)
                && !StringUtil.isEmpty(key2)) {
            return true;
        }
        return false;
    }

    public String getSearchType1() {
        return searchType1;
    }

    public void setSearchType1(String searchType1) {
        this.searchType1 = searchType1;
    }

    public String getSearchType2() {
        return searchType2;
    }

    public void setSearchType2(String searchType2) {
        this.searchType2 = searchType2;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getCmctStatus() {
        return cmctStatus;
    }

    public void setCmctStatus(String cmctStatus) {
        this.cmctStatus = cmctStatus;
    }

    public String getCmctWay() {
        return cmctWay;
    }

    public void setCmctWay(String cmctWay) {
        this.cmctWay = cmctWay;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getkey1Begin() {
        return key1Begin;
    }

    public void setkey1Begin(String key1Begin) {
        this.key1Begin = key1Begin;
    }

    public String getkey1End() {
        return key1End;
    }

    public void setkey1End(String key1End) {
        this.key1End = key1End;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
