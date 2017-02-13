package com.talebase.cloud.base.ms.project.dto;

/**
 * Created by kanghong.zhao on 2016-12-21.
 */
public class DProjectCopyReq {

    private Integer sourceProjectId;
    private String name;

    public DProjectCopyReq(){}

    public DProjectCopyReq(Integer sourceProjectId, String name) {
        this.sourceProjectId = sourceProjectId;
        this.name = name;
    }

    public Integer getSourceProjectId() {
        return sourceProjectId;
    }

    public void setSourceProjectId(Integer sourceProjectId) {
        this.sourceProjectId = sourceProjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
