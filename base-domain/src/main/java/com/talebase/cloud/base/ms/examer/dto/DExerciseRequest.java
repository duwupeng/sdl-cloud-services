package com.talebase.cloud.base.ms.examer.dto;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author auto-tool
 */
public class DExerciseRequest {
    /**
     *
     */
    Integer id;

    /**
     *
     */
    Integer userId;

    /**
     *
     */
    Integer taskId;

    /**
     *
     */
    Integer paperId;

    /**
     * 答题开始时间
     */
    String startTime;

    /**
     * 答题提交时间
     */
    String endTime;

    /**
     * 总分
     */
    String subScore;

    /**
     * 得分明细
     */
    List<DAnswerScoreDetail> dAnswerScoreDetails;

    /**
     * 考生序号(根据交卷)
     */
    Integer serialNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSubScore() {
        return subScore;
    }

    public void setSubScore(String subScore) {
        this.subScore = subScore;
    }

    public List<DAnswerScoreDetail> getdAnswerScoreDetails() {
        return dAnswerScoreDetails;
    }

    public void setdAnswerScoreDetails(List<DAnswerScoreDetail> dAnswerScoreDetails) {
        this.dAnswerScoreDetails = dAnswerScoreDetails;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }
}
