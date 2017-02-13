package com.talebase.cloud.base.ms.examer.dto;

import java.util.Date;

/**
 * Created by kanghong.zhao on 2017-1-13.
 */
public class DUserExamPermission {

    private Integer taskStatus;
    private Integer projectStatus;
    private Date taskStartDate;
    private Date taskEndDate;
    private Date taskLatestStartDate;
    private Integer userExamStatus;
    private Date exerciseStartTime;
    private Date exerciseEndTime;
    private Integer taskExamTime;
    private Integer taskPageChangeLimit;
    private String projectAdmin;//项目拥有人id
    private String examerAdmin;//考生创建人id
    private String projectName;
    private Integer projectId;
    private String taskName;
    private Integer taskId;
    private Integer taskPaperId;
    private Integer exercisePaperId;
//    private Integer

    public Integer getTaskPaperId() {
        return taskPaperId;
    }

    public void setTaskPaperId(Integer taskPaperId) {
        this.taskPaperId = taskPaperId;
    }

    public Integer getExercisePaperId() {
        return exercisePaperId;
    }

    public void setExercisePaperId(Integer exercisePaperId) {
        this.exercisePaperId = exercisePaperId;
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

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(Date taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public Date getTaskLatestStartDate() {
        return taskLatestStartDate;
    }

    public void setTaskLatestStartDate(Date taskLatestStartDate) {
        this.taskLatestStartDate = taskLatestStartDate;
    }

    public Integer getUserExamStatus() {
        return userExamStatus;
    }

    public void setUserExamStatus(Integer userExamStatus) {
        this.userExamStatus = userExamStatus;
    }

    public Date getExerciseStartTime() {
        return exerciseStartTime;
    }

    public void setExerciseStartTime(Date exerciseStartTime) {
        this.exerciseStartTime = exerciseStartTime;
    }

    public Date getExerciseEndTime() {
        return exerciseEndTime;
    }

    public void setExerciseEndTime(Date exerciseEndTime) {
        this.exerciseEndTime = exerciseEndTime;
    }

    public Integer getTaskExamTime() {
        return taskExamTime;
    }

    public void setTaskExamTime(Integer taskExamTime) {
        this.taskExamTime = taskExamTime;
    }

    public Integer getTaskPageChangeLimit() {
        return taskPageChangeLimit;
    }

    public void setTaskPageChangeLimit(Integer taskPageChangeLimit) {
        this.taskPageChangeLimit = taskPageChangeLimit;
    }

    public String getProjectAdmin() {
        return projectAdmin;
    }

    public void setProjectAdmin(String projectAdmin) {
        this.projectAdmin = projectAdmin;
    }

    public String getExamerAdmin() {
        return examerAdmin;
    }

    public void setExamerAdmin(String examerAdmin) {
        this.examerAdmin = examerAdmin;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return "DUserExamPermission{" +
                "taskStatus=" + taskStatus +
                ", projectStatus=" + projectStatus +
                ", taskStartDate=" + taskStartDate +
                ", taskEndDate=" + taskEndDate +
                ", taskLatestStartDate=" + taskLatestStartDate +
                ", userExamStatus=" + userExamStatus +
                ", exerciseStartTime=" + exerciseStartTime +
                ", exerciseEndTime=" + exerciseEndTime +
                ", taskExamTime=" + taskExamTime +
                ", taskPageChangeLimit=" + taskPageChangeLimit +
                ", projectAdmin='" + projectAdmin + '\'' +
                ", examerAdmin='" + examerAdmin + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectId=" + projectId +
                ", taskName='" + taskName + '\'' +
                ", taskId=" + taskId +
                ", taskPaperId=" + taskPaperId +
                ", exercisePaperId=" + exercisePaperId +
                '}';
    }
}
