package com.talebase.cloud.base.ms.notify.enumes;

/**
 * 发送方法
 **/
public enum TNotifyTemplateMethods {
    /**
     * 邮件
     **/
    MAIL(0),

    /**
     * 短信
     **/
    SMS(1),

    /**
     * 微信
     **/
    WECHAT(2);
    private final int value;

    private TNotifyTemplateMethods(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
