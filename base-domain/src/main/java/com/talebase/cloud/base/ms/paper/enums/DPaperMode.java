package com.talebase.cloud.base.ms.paper.enums;

/**
 * 模式,1:创建中(creating);2:修改中(modifying);3:完成(completed)
 **/
public enum DPaperMode {
    /**
     * 否
     **/
    新建中(1),

    /**
     * 是
     **/
    修改中(2),

    /**
     * 是
     **/
    完成(3);

    private final int value;

    private DPaperMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
