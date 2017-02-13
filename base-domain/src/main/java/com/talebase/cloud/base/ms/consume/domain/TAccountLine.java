package com.talebase.cloud.base.ms.consume.domain;

import java.util.Date;

/**
 * Created by suntree.xu on 2016-12-7.
 */
public class TAccountLine {
    /**
     * id
     */
    Integer id;

    /**
     *公司id
     */
    Integer companyId;

    /**
     * 操作人账号
     */
    String modifier;

    /**
     * 操作时间
     */
    Date modifiedDate;

    /**
     * 操作备注
     */
    String remark;

    /**
     * 流水类型,1:消费;2:充值
     */
    Integer type;

    /**
     *本次流水点数变量
     */
    Integer pointVar = 0;

    /**
     * 本次流水短信变量
     */
    Integer smsVar = 0;

    /**
     * 相关操作的项目id
     */
    Integer projectId;

    /**
     * 相关操作的任务d
     */
    Integer taskId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPointVar() {
        return pointVar;
    }

    public void setPointVar(Integer pointVar) {
        this.pointVar = pointVar;
    }

    public Integer getSmsVar() {
        return smsVar;
    }

    public void setSmsVar(Integer smsVar) {
        this.smsVar = smsVar;
    }

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
}
