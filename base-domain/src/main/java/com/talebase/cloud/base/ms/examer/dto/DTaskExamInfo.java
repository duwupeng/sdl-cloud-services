package com.talebase.cloud.base.ms.examer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2017-2-8.
 */
public class DTaskExamInfo {

    private Integer projectId;
    private String projectName;
    private Integer taskId;
    private String taskName;

    public DTaskExamInfo(){}

    public DTaskExamInfo(Integer projectId, String projectName, Integer taskId, String taskName) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.taskId = taskId;
        this.taskName = taskName;
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
}
