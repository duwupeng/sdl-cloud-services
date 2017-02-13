package com.talebase.cloud.base.ms.project.dto;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-22.
 */
public class DExamTaskResponse {
    Integer projectId;
    String projectName;
    String description;
    Timestamp projectStartDate;
    Timestamp projectEndDate;
    List<DExamTasks> dExamTasksList;

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

    public List<DExamTasks> getdExamTasksList() {
        return dExamTasksList;
    }

    public void setdExamTasksList(List<DExamTasks> dExamTasksList) {
        this.dExamTasksList = dExamTasksList;
    }
}
