package com.talebase.cloud.base.ms.consume.dto;

/**
 * Created by suntree.xu on 2016-12-8.
 */
public class DAccountComsumeDisplay {
    private String account;
    private int pointUsed;
    private String projectName;
    private int smsUsed;
    private String taskName;
    private int dredgeCount;//开通人次
    private int usedCount;//使用人次


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

    public int getSmsUsed() {
        return smsUsed;
    }

    public void setSmsUsed(int smsUsed) {
        this.smsUsed = smsUsed;
    }

    public int getDredgeCount() {
        return dredgeCount;
    }

    public void setDredgeCount(int dredgeCount) {
        this.dredgeCount = dredgeCount;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }
}
