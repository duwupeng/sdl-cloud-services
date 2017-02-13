package com.talebase.cloud.base.ms.project.dto;

/**
 * Created by kanghong.zhao on 2016-12-6.
 */
public class DTaskSelect {

    private Integer id;
    private Integer projectId;
    private String name;

    public DTaskSelect(){}

    public DTaskSelect(Integer id, Integer projectId, String name) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
