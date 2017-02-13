package com.talebase.cloud.base.ms.paper.enums;

/**
 * 上传题目类型
 **/
public enum TAttachmentType {
    /**
     * 图片
     **/
    PIC("1"),

    /**
     * 文档
     **/
    WORD("2"),

    /**
     * 工作表
     **/
    EXCEL("3");
    private final String value;

    private TAttachmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
