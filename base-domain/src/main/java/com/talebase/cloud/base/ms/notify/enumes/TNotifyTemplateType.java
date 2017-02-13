package com.talebase.cloud.base.ms.notify.enumes;

/**
 * 类型
 **/
public enum TNotifyTemplateType {
    /**
     * 系统
     **/
    SYSTEM(0),

    /**
     * 自定义
     **/
    CUSTOMIZE(1);
    private final int value;

    private TNotifyTemplateType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
