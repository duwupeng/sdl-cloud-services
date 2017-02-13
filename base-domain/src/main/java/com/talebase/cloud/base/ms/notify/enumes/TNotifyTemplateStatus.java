package com.talebase.cloud.base.ms.notify.enumes;

/**
 * 状态
 **/
public enum TNotifyTemplateStatus {
    /**
     * 禁用
     **/
    DISABLE(0),

    /**
     * 启用
     **/
    ENABLE(1),

    /**
     * 删除
     **/
    DELETE(2);
    private final int value;

    private TNotifyTemplateStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
