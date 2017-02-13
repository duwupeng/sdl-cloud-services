package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;

/**
 * 创建试卷头
 * Created by eric.du on 2016-12-6.
 */
public class DWPaper1 extends DItem {
    /**
     * 试卷名称
     */
    String name;

    /**
     * 描述/说明
     */
    String comment;

    /**
     * 限时
     */
    Integer duration;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
