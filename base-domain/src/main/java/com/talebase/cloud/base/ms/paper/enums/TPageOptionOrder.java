package com.talebase.cloud.base.ms.paper.enums;

/**
 * 选项乱序
 **/
public enum TPageOptionOrder {
    /**
     * 否
     **/
    NO(0),

    /**
     * 是
     **/
    YES(1);
    private final int value;

    private TPageOptionOrder(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
