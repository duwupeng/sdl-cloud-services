package com.talebase.cloud.base.ms.paper.enums;

/**
 * 题型
 **/
public enum TBlankType {
    /**
     * 主观
     **/
    SUBJECTIVE(0),

    /**
     * 客观
     **/
    OBJECTIVE(1);
    private final int value;

    private TBlankType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
