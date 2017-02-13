package com.talebase.cloud.base.ms.examer.dto;

import java.util.List;

/**
 * @author zhangchunlin
 */public class DUserShowFieldReq {
    /**
     * 公司id
     */
    Integer companyId;
    /**
     * 任务id
     */
    List<Integer> taskIds;

    /**
     * 项目id
     */
    List<Integer> projectIds;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<Integer> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Integer> taskIds) {
        this.taskIds = taskIds;
    }

    public List<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Integer> projectIds) {
        this.projectIds = projectIds;
    }
}
