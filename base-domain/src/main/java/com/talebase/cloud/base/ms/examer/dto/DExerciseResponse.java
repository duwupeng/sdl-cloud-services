package com.talebase.cloud.base.ms.examer.dto;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author auto-tool
 */
public class DExerciseResponse {
    /**
     *
     */
    Integer id;

    /**
     * 考生编号
     */
    Integer userId;

    /**
     * 任务编号
     */
    String taskId;

    /**
     * 试卷编号
     */
    Integer paperId;

    /**
     * 答题开始时间
     */
    Long startTime;

    /**
     * 答题提交时间
     */
    Long endTime;

    /**
     * 主观题得分
     */
    String subScore;

    /**
     * 得分明细
     */
    List<DAnswerScoreDetail> answerScoreDetail;

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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getSubScore() {
        return subScore;
    }

    public void setSubScore(String subScore) {
        this.subScore = subScore;
    }

    public List<DAnswerScoreDetail> getAnswerScoreDetail() {
        return answerScoreDetail;
    }

    public void setAnswerScoreDetail(List<DAnswerScoreDetail> answerScoreDetail) {
        this.answerScoreDetail = answerScoreDetail;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }
}
