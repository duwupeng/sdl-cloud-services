package com.talebase.cloud.base.ms.paper.enums;

/**
 * 卷类型
 **/
public enum TPaperType {
    /**
     * 心理测评
     **/
    PSYCHOLOGICAL(0),

    /**
     * 360测评
     **/
    EVALUATION360(1),

    /**
     * 考试
     **/
    EXAM(2),

    /**
     * 敬业度
     **/
    ENGAGEMENT(3),

    /**
     * 调研
     **/
    RESEARCH(4);
    private final int value;

    private TPaperType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
