package com.talebase.cloud.base.ms.notify.enumes;

/**
 * 发送类型
 **/
public enum DNotifyRecordSendType {
    /**
     * 邮件
     **/
    MAIL(0),

    /**
     * 短信
     **/
    SMS(1),

    /**
     * 邮件与短信
     **/
    MAIL_AND_SMS(2),

    /**
     * 微信
     **/
    WECHAT(3);

    private final int value;

    private DNotifyRecordSendType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
