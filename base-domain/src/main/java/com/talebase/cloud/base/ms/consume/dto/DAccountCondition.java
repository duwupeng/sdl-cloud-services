package com.talebase.cloud.base.ms.consume.dto;

import com.talebase.cloud.base.ms.admin.dto.DGroupAndRole;
import com.talebase.cloud.base.ms.project.dto.DProjectSelect;

import java.util.List;

/**
 * 查询消费记录条件
 * Created by suntree.xu on 2016-12-7.
 */
public class DAccountCondition {
    String account;
    int projectId;
    int taskId;

    private List<DProjectSelect> dProjectSelectList;

    int pointCount;
    int smsCount;

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public List<DProjectSelect> getdProjectSelectList() {
        return dProjectSelectList;
    }

    public void setdProjectSelectList(List<DProjectSelect> dProjectSelectList) {
        this.dProjectSelectList = dProjectSelectList;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
