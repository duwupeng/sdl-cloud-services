package com.talebase.cloud.base.ms.notify.enumes;

/**
 * 是否默认
 **/
public enum TNotifyTemplateWhetherDefault {
    /**
     * 否
     **/
    NO(0),

    /**
     * 是
     **/
    YES(1);
    private final int value;

    private TNotifyTemplateWhetherDefault(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
