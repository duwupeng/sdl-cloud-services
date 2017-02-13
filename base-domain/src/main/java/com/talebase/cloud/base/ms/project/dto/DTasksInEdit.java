package com.talebase.cloud.base.ms.project.dto;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-15.
 */
public class DTasksInEdit {

    private List<DTaskInEdit> tasks;
    private Long sysTimeL;
    private Long projectStartDateL;
    private Long projectEndDateL;

    public List<DTaskInEdit> getTasks() {
        return tasks;
    }

    public void setTasks(List<DTaskInEdit> tasks) {
        this.tasks = tasks;
    }

    public Long getSysTimeL() {
        return sysTimeL;
    }

    public void setSysTimeL(Long sysTimeL) {
        this.sysTimeL = sysTimeL;
    }

    public Long getProjectStartDateL() {
        return projectStartDateL;
    }

    public void setProjectStartDateL(Long projectStartDateL) {
        this.projectStartDateL = projectStartDateL;
    }

    public Long getProjectEndDateL() {
        return projectEndDateL;
    }

    public void setProjectEndDateL(Long projectEndDateL) {
        this.projectEndDateL = projectEndDateL;
    }

    public DTasksInEdit(List<DTaskInEdit> tasks, Long sysTimeL, Long projectStartDateL, Long projectEndDateL) {
        this.projectEndDateL = projectEndDateL;
        this.tasks = tasks;
        this.sysTimeL = sysTimeL;
        this.projectStartDateL = projectStartDateL;
    }

    public DTasksInEdit(){}
}
