package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;

/**
 * Created by bin.yang on 2016-12-7.
 */
public class DOptionScoreDetail {
    /**
     * 编码标识
     */
    String maskCode;
    /**
     * 分数
     */
    BigDecimal score;

    public String getMaskCode() {
        return maskCode;
    }

    public void setMaskCode(String maskCode) {
        this.maskCode = maskCode;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
