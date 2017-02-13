package com.talebase.cloud.base.ms.consume.dto;

import java.util.Date;

/**
 * 查询消费记录结果
 * Created by suntree.xu on 2016-12-7.
 */
public class DAccountConsumeResult {
    private String account;
    private int pointUsed;
    private int projectId;
    private int smsUsed;
    private int taskId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getPointUsed() {
        return pointUsed;
    }

    public void setPointUsed(int pointUsed) {
        this.pointUsed = pointUsed;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getSmsUsed() {
        return smsUsed;
    }

    public void setSmsUsed(int smsUsed) {
        this.smsUsed = smsUsed;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
