package com.talebase.cloud.base.ms.paper.enums;

/**
 * 计分规则
 **/
public enum TBlankScoreRule {
    /**
     * 完全一致
     **/
    IN_FULL_ACCORD(0),

    /**
     * 仅顺序不一致
     **/
    SEQUENTIAL_INCONSISTENCY(1),

    /**
     * 仅供参考
     */

    FOR_REFERENCE_ONLY(2);

    private final int value;

    private TBlankScoreRule(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
