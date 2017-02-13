package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DPaper extends DItem {

    /**
     * 试卷名称
     */
    String name;

    /**
     * 描述/说明
     */
    String comment;

    String composer;

    /**
     * 限时
     */
    Integer duration;

    /**
     * 使用次数
     */
    Integer usage;

    /**
     * 总题量
     */
    Integer totalNum;

    /**
     * 主观题量
     */
    Integer subjectNum;


    Long createdDate;

    boolean status;

    /**
     * 模式,0:创建中(creating);1:修改中(modifying);2:完成(completed)
     */
    Integer mode;

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

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getSubjectNum() {
        return subjectNum;
    }

    public void setSubjectNum(Integer subjectNum) {
        this.subjectNum = subjectNum;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DPaper dPaper = (DPaper) o;

        if (comment != null && dPaper.comment != null && !comment.equals(dPaper.comment))
            return false;
        if (duration != null && dPaper.score != null && score.compareTo(dPaper.score) != 0)
            return false;
        return name.equals(dPaper.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + comment.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }
}
