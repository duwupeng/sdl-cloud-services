package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;

/**
 * 试卷列表信息
 * Created by eric.du on 2016-12-6.
 */
public class DWPaper {
    /**
     *
     */
    Integer id;

    /**
     * 试卷名称
     */
    String name;

    /**
     * 总题量
     */
    Integer totalNum;

    /**
     * 总分数
     */
    BigDecimal totalScore;

    /**
     * 试卷时长
     */
    Integer duration;

    /**
     * 使用次数
     */
    Integer usage;

    /**
     * 创建人
     */
    String creator;

    /**
     * 创建时间
     */
    Long createdDate;
    /**
     * 状态, 启用  弃用
     */
    boolean status;

    /**
     * 模式,0:创建中(creating);1:修改中(modifying);2:完成(completed)
     */
    Integer mode;
    /**
     * 唯一标识编码
     */
    String unicode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }
}
