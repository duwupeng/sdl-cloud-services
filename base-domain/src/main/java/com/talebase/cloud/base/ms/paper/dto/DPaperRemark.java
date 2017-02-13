package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DPaperRemark  extends DItem{
    /**
     * 起始分数
     */
    BigDecimal startScore;

    /**
     * 结束分数
     */
    BigDecimal endScore;

    /**
     * 结束语
     */
    String description;

    public BigDecimal getStartScore() {
        return startScore;
    }

    public void setStartScore(BigDecimal startScore) {
        this.startScore = startScore;
    }

    public BigDecimal getEndScore() {
        return endScore;
    }

    public void setEndScore(BigDecimal endScore) {
        this.endScore = endScore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
