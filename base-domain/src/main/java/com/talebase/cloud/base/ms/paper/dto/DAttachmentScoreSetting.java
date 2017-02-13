package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DAttachmentScoreSetting {
    /**
     * 该题分数
     */
    BigDecimal score;

    /**
     * 评分标准
     */
    String scoreRule;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getScoreRule() {
        return scoreRule;
    }

    public void setScoreRule(String scoreRule) {
        this.scoreRule = scoreRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DAttachmentScoreSetting dAttachmentScoreSetting = (DAttachmentScoreSetting) o;

        if (scoreRule != dAttachmentScoreSetting.scoreRule) return false;
        return score == dAttachmentScoreSetting.score;
    }

    @Override
    public int hashCode() {
        int result = scoreRule.hashCode();
        result = 31 * result + score.hashCode();
        return result;
    }
}
