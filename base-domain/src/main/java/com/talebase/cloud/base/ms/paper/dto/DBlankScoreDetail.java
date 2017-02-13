package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;

/**
 * Created by bin.yang on 2016-12-7.
 */
public class DBlankScoreDetail {
    /**
     * 填空第几个
     */
    Integer seqNo;
    /**
     * 填空题答案
     */
    String answer;
    /**
     * 分数
     */
    BigDecimal score  = new BigDecimal(0);

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
