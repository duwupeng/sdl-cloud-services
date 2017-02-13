package com.talebase.cloud.base.ms.login.enumes;

/**
 * Created by zhangchunlin on 2016-12-14.
 */
public enum LoginTypeEnume {
    /**
     * 考生
     */
    EXAMINEE(0),

    /**
     * 管理员
     */
    ADMIN(1);

    private final int value;
    private LoginTypeEnume(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
