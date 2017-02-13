package com.talebase.cloud.base.ms.examer.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TExercise {
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
    Integer taskId;

    /**
     * 试卷编号
     */
    Integer paperId;

    /**
     * 答题开始时间
     */
    java.sql.Timestamp startTime;

    /**
     * 答题提交时间
     */
    java.sql.Timestamp endTime;

    /**
     * 得分明细
     */
    String answerScoreDetail;

    /**
     * 主观题得分
     */
    BigDecimal subScore;

    /**
     * 考生序号(根据交卷)
     */
    Integer serialNo;

    /**
     * 客观题得分
     */
    BigDecimal objScore;

    /**
     * 交卷类型:0:考生交卷,1:系统定时交卷
     */
    Integer submitType;

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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getAnswerScoreDetail() {
        return answerScoreDetail;
    }

    public void setAnswerScoreDetail(String answerScoreDetail) {
        this.answerScoreDetail = answerScoreDetail;
    }

    public BigDecimal getSubScore() {
        return subScore;
    }

    public void setSubScore(BigDecimal subScore) {
        this.subScore = subScore;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public BigDecimal getObjScore() {
        return objScore;
    }

    public void setObjScore(BigDecimal objScore) {
        this.objScore = objScore;
    }

    public Integer getSubmitType() {
        return submitType;
    }

    public void setSubmitType(Integer submitType) {
        this.submitType = submitType;
    }
}
