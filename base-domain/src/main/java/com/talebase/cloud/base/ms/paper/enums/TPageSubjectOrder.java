package com.talebase.cloud.base.ms.paper.enums;

/**
 * 题目乱序
 **/
public enum TPageSubjectOrder {
    /**
     * 否
     **/
    NO(0),

    /**
     * 是
     **/
    YES(1);
    private final int value;

    private TPageSubjectOrder(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
