package com.talebase.cloud.base.ms.paper.enums;

/**
 * 版本类型
 **/
public enum DVersionType {
    /**
     * 小版本
     **/
    LITTLE_VERSION(0),
    /**
     * 大版本
     **/
    BIG_VERSION(1);

    private final int value;

    private DVersionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
