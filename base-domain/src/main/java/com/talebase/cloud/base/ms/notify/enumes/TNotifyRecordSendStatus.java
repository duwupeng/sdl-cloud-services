package com.talebase.cloud.base.ms.notify.enumes;

/**
 * 发送状态
 **/
public enum TNotifyRecordSendStatus {
    /**
     * 发送失败
     **/
    SEND_FAIL(0),

    /**
     * 发送成功
     **/
    SEND_SUCCESS(1),

    /**
     * 发送中
     **/
    SENDING(2);
    private final int value;

    private TNotifyRecordSendStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
