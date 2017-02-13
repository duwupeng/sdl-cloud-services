package com.talebase.cloud.base.ms.paper.enums;

/**
 * 题型
 **/
public enum TOptionType {
    /**
     * 单选题
     **/
    SINGLE_CHOICE(4),

    /**
     * 多选题
     **/
    MULTIPLE_CHOICE(5),

    /**
     * 判断题
     **/
    TRUE_FALSE(2);

    private final int value;

    private TOptionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
