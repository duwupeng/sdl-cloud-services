package com.talebase.cloud.base.ms.examer.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
public class DEditExamerReq {

    private Integer projectId;
    private Integer taskId;
    private List<Integer> taskIds;
    private Map<String, String> map;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public List<Integer> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Integer> taskIds) {
        this.taskIds = taskIds;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public DEditExamerReq(Integer projectId, Integer taskId, Map<String, String> map) {
        this.projectId = projectId;
        this.taskId = taskId;
        this.map = map;
    }

    public DEditExamerReq(){}

}
