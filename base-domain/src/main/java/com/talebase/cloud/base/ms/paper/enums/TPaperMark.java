package com.talebase.cloud.base.ms.paper.enums;

/**
 * 是否需要阅卷
 **/
public enum TPaperMark {
    /**
     * 否
     **/
    NO(0),

    /**
     * 是
     **/
    YES(1);
    private final int value;

    private TPaperMark(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
