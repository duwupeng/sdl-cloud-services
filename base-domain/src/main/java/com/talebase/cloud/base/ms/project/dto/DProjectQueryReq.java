package com.talebase.cloud.base.ms.project.dto;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class DProjectQueryReq {

    private String projectNameLike;
    private String taskNameLike;

    private Long sysTimeL;//返回的系统时间；不作为查询条件处理

    public String getProjectNameLike() {
        return projectNameLike;
    }

    public void setProjectNameLike(String projectNameLike) {
        this.projectNameLike = projectNameLike;
    }

    public String getTaskNameLike() {
        return taskNameLike;
    }

    public void setTaskNameLike(String taskNameLike) {
        this.taskNameLike = taskNameLike;
    }

    public Long getSysTimeL() {
        return sysTimeL;
    }

    public void setSysTimeL(Long sysTimeL) {
        this.sysTimeL = sysTimeL;
    }
}
