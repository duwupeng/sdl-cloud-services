package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
public class DCreateExamersReq {

    private Integer projectId;
    private Integer taskId;
    private String accountPre;
    private String password;
    private Integer amount;
    private Integer startNum;
    private Integer endNum;
    private String suffix = "";

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

    public String getAccountPre() {
        return accountPre;
    }

    public void setAccountPre(String accountPre) {
        this.accountPre = accountPre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getEndNum() {
        return endNum;
    }

    public void setEndNum(Integer endNum) {
        this.endNum = endNum;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
