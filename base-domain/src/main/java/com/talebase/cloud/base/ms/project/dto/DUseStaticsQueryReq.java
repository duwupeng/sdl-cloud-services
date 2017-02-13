package com.talebase.cloud.base.ms.project.dto;

/**
 * Created by kanghong.zhao on 2016-12-22.
 */
public class DUseStaticsQueryReq {

    private String account;
    private Integer projectId;
    private Integer taskId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public DUseStaticsQueryReq(String account, Integer projectId, Integer taskId) {
        this.account = account;
        this.projectId = projectId;
        this.taskId = taskId;
    }

    public DUseStaticsQueryReq(){}
}
