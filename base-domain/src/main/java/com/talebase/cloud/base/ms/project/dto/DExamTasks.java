package com.talebase.cloud.base.ms.project.dto;

import java.sql.Timestamp;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class DExamTasks {

    private Integer taskId;
    private String taskName;
    private Timestamp taskStartDate;//任务开始时间
    private Timestamp taskEndDate;//任务结束时间
    private Integer paperNum;//题目数
    private String examTime;//参考用时
    private Integer paperId;//试卷编号
    private Integer taskStatus;
    private String examineeStatus;
    private Boolean canAnswer = false;
    private String tips = "未知异常";

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Timestamp getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Timestamp taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Timestamp getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(Timestamp taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public Integer getPaperNum() {
        return paperNum;
    }

    public void setPaperNum(Integer paperNum) {
        this.paperNum = paperNum;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getExamineeStatus() {
        return examineeStatus;
    }

    public void setExamineeStatus(String examineeStatus) {
        this.examineeStatus = examineeStatus;
    }

    public Boolean getCanAnswer() {
        return canAnswer;
    }

    public void setCanAnswer(Boolean canAnswer) {
        this.canAnswer = canAnswer;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
