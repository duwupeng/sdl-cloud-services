package com.talebase.cloud.base.ms.notify.enumes;

/**
 * 是否默认
 **/
public enum TNotifyTemplateDefault {
    /**
     * 否
     **/
    NO(0),

    /**
     * 是
     **/
    YES(1);
    private final int value;

    private TNotifyTemplateDefault(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
