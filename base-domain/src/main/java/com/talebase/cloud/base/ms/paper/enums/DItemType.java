package com.talebase.cloud.base.ms.paper.enums;

/**
 * Created by bin.yang on 2016-12-28.
 * 元素类型
 */
public enum DItemType {

    /**
     * 试卷信息
     */
    PAPER_INFO(1),

    /**
     * 页码
     **/
    PAGE(2),

    /**
     * 说明
     **/
    INSTRUCTION(3),

    /**
     * 单择题
     **/
    SINGLE_CHOICE(4),

    /**
     * 多择题
     **/
    MULTIPLE_CHOICE(5),

    /**
     * 填空题
     **/
    BLANK(6),

    /**
     * 上传题
     **/
    ATTACHMENT(7),

    /**
     * 结束语
     **/
    REMARK(8);

    private final int value;

    private DItemType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
