package com.talebase.cloud.base.ms.examer.dto;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-9.
 */
public class DImportReq {

    private Integer projectId;
    private Integer taskId;
    private List<String> header;
    private List<List<String>> details;

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

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<List<String>> getDetails() {
        return details;
    }

    public void setDetails(List<List<String>> details) {
        this.details = details;
    }

    public DImportReq(Integer projectId, Integer taskId, List<String> header, List<List<String>> details) {
        this.projectId = projectId;
        this.taskId = taskId;
        this.header = header;
        this.details = details;
    }

    public DImportReq(){}
}
