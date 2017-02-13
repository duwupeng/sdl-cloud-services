package com.talebase.cloud.base.ms.consume.dto;

/**
 * Created by suntree.xu on 2016-12-8.
 */
public class DAccountLineOperateCondition {
    private int pointVar;        //	点数修改额	number
    private int projectId;          //	项目id	number
    private String remark;	       // 操作备注	string
    private int smsVar;          //	短信修改额	number
    private int taskId;             //任务id	number



    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPointVar() {
        return pointVar;
    }

    public void setPointVar(int pointVar) {
        this.pointVar = pointVar;
    }

    public int getSmsVar() {
        return smsVar;
    }

    public void setSmsVar(int smsVar) {
        this.smsVar = smsVar;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
