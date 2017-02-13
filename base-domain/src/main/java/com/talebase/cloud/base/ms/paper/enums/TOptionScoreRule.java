package com.talebase.cloud.base.ms.paper.enums;

/**
 * 计分规则
 **/
public enum TOptionScoreRule {
    /**
     * 全部答对才给分
     **/
    ALL(0),

    /**
     * 少选统一给分
     **/
    PART_UNITY(1),

    /**
     * 按比例给分
     **/
    PART_AVG(2);
    private final int value;

    private TOptionScoreRule(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
