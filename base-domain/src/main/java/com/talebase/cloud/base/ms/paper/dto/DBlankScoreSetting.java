package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DBlankScoreSetting {
    /**
     * 答案. blank_1:mask_code1|mask_code2 -> 1;blank_2:mask_code3 -> 2
     */
    List<DBlankScoreDetail> dBlankScoreDetails;
    /**
     * 总分
     */
    BigDecimal score = new BigDecimal(0);

    /**
     * 计分规则,0:完全一致；1：仅顺序不一致;2:仅供参考
     */
    Integer scoreRule;

    String explanation;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getScoreRule() {
        return scoreRule;
    }

    public void setScoreRule(Integer scoreRule) {
        this.scoreRule = scoreRule;
    }

    public List<DBlankScoreDetail> getdBlankScoreDetails() {
        return dBlankScoreDetails;
    }

    public void setdBlankScoreDetails(List<DBlankScoreDetail> dBlankScoreDetails) {
        this.dBlankScoreDetails = dBlankScoreDetails;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBlankScoreSetting dBlankScoreSetting = (DBlankScoreSetting) o;

        if (!dBlankScoreDetails.equals(dBlankScoreSetting.dBlankScoreDetails)) return false;
        if (scoreRule != dBlankScoreSetting.scoreRule) return false;
        if (score != dBlankScoreSetting.score) return false;
        return explanation.equals(dBlankScoreSetting.explanation);
    }

    @Override
    public int hashCode() {
        int result = dBlankScoreDetails.hashCode();
        result = 31 * result + scoreRule.hashCode();
        result = 31 * result + score.hashCode();
        result = 31 * result + explanation.hashCode();
        return result;
    }
}
