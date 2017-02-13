package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by bin.yang on 2016-12-21.
 */
public class DDataManagementRequest {
    Integer projectId;
    Integer taskId;

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

    public DDataManagementRequest(Integer projectId, Integer taskId){
        this.projectId = projectId;
        this.taskId = taskId;
    }

    public DDataManagementRequest(){}
}
