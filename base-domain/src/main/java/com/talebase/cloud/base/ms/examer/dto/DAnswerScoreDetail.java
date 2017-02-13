package com.talebase.cloud.base.ms.examer.dto;

import java.math.BigDecimal;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DAnswerScoreDetail {
    /**
     * 题目序号
     */
    Integer seqNo;
    /**
     * 题目类型
     */
    Integer type;
    /**
     * 题目ID
     */
    Integer subjectId;
    /**
     * 标准答案
     */
    String standwardAnswer;
    /**
     * 标准分数
     */
    BigDecimal standwardScore;
    /**
     * 考生答案
     */
    String examerAnswer;
    /**
     * 考生得分
     */
    BigDecimal examerScore;

    public String getStandwardAnswer() {
        return standwardAnswer;
    }

    public void setStandwardAnswer(String standwardAnswer) {
        this.standwardAnswer = standwardAnswer;
    }

    public BigDecimal getStandwardScore() {
        return standwardScore;
    }

    public void setStandwardScore(BigDecimal standwardScore) {
        this.standwardScore = standwardScore;
    }

    public String getExamerAnswer() {
        return examerAnswer;
    }

    public void setExamerAnswer(String examerAnswer) {
        this.examerAnswer = examerAnswer;
    }

    public BigDecimal getExamerScore() {
        return examerScore;
    }

    public void setExamerScore(BigDecimal examerScore) {
        this.examerScore = examerScore;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }
}
