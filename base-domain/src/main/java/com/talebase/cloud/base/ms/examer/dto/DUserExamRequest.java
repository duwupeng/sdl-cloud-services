package com.talebase.cloud.base.ms.examer.dto;



/**
 * Created by zhangchunlin on 2016-12-20.
 */
public class DUserExamRequest {

    private Integer userId;
    private Integer projectId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
