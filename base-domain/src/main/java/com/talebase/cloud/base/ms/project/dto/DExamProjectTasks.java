package com.talebase.cloud.base.ms.project.dto;

import java.sql.Timestamp;

/**
 * Created by bin.yang on 2016-12-22.
 */
public class DExamProjectTasks {
    Integer projectId;
    String projectName;
    String description;
    Timestamp projectStartDate;
    Timestamp projectEndDate;
    Integer projectStatus;
    Integer taskId;
    String taskName;
    Integer taskStatus;
    Timestamp taskStartDate;
    Timestamp taskEndDate;
    Timestamp taskLatestEndDate;
    Integer paperNum;
    Integer examTime;
    Integer paperId;
    String examineeStatus;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Timestamp projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Timestamp getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(Timestamp projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
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

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Timestamp getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Timestamp taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Timestamp getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(Timestamp taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public Integer getPaperNum() {
        return paperNum;
    }

    public void setPaperNum(Integer paperNum) {
        this.paperNum = paperNum;
    }

    public Integer getExamTime() {
        return examTime;
    }

    public void setExamTime(Integer examTime) {
        this.examTime = examTime;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getExamineeStatus() {
        return examineeStatus;
    }

    public Timestamp getTaskLatestEndDate() {
        return taskLatestEndDate;
    }

    public void setTaskLatestEndDate(Timestamp taskLatestEndDate) {
        this.taskLatestEndDate = taskLatestEndDate;
    }

    public void setExamineeStatus(String examineeStatus) {
        this.examineeStatus = examineeStatus;
    }
}
