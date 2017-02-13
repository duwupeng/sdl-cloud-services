package com.talebase.cloud.base.ms.project.dto;

import java.util.Date;

/**
 * Created by kanghong.zhao on 2016-12-19.
 */
public class DTaskInScore {

    private String projectName;
    private Integer projectId;
    private Integer taskId;
    private String taskName;
    private Long projectStartDateL;
    private Long projectEndDateL;
    private Integer needMarkingNum;//主观题题量
    private Integer finishNum;//交卷人数
    private Integer markedNum;//已评卷人数

    private Date projectStartDate;
    private Date projectEndDate;

    private String accountNames;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getProjectStartDateL() {
        return projectStartDateL;
    }

    public void setProjectStartDateL(Long projectStartDateL) {
        this.projectStartDateL = projectStartDateL;
    }

    public Long getProjectEndDateL() {
        return projectEndDateL;
    }

    public void setProjectEndDateL(Long projectEndDateL) {
        this.projectEndDateL = projectEndDateL;
    }

    public Integer getNeedMarkingNum() {
        return needMarkingNum;
    }

    public void setNeedMarkingNum(Integer needMarkingNum) {
        this.needMarkingNum = needMarkingNum;
    }

    public Integer getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(Integer finishNum) {
        this.finishNum = finishNum;
    }

    public Integer getMarkedNum() {
        return markedNum;
    }

    public void setMarkedNum(Integer markedNum) {
        this.markedNum = markedNum;
    }

    public String getAccountNames() {
        return accountNames;
    }

    public void setAccountNames(String accountNames) {
        this.accountNames = accountNames;
    }

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Date getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(Date projectEndDate) {
        this.projectEndDate = projectEndDate;
    }
}
