package com.talebase.cloud.base.ms.paper.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TPaper {
    /**
     * 自增长主键
     */
    Integer id;

    /**
     *
     */
    Integer companyId;

    /**
     * 考卷编号
     */
    String unicode;

    /**
     * 版本
     */
    BigDecimal version;

    /**
     * 版本类型0：大版本，1：小版本
     */
    Integer versionType;

    /**
     * 组题
     */
    String composer;

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

    /**
     * 卷类型,0:心理测评(psychological);1:360测评(evaluation360);2:考试(exam);3:敬业度(engagement);4:调研(research)
     */
    Integer type;

    /**
     * 是否需要阅卷,0:否(no);1:是(yes)
     */
    Integer mark;

    /**
     * 总分数
     */
    java.math.BigDecimal score;

    /**
     * 状态,0:未启用(disabled);1:已启用(enabled);3:已删除(deleted)
     */
    Integer status;

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

    /**
     * 模式,0:创建中(creating);1:修改中(modifying);2:完成(completed)
     */
    Integer mode;

    /**
     * 创建人
     */
    String creator;

    /**
     * 创建日期
     */
    java.sql.Timestamp createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

    public Integer getVersionType() {
        return versionType;
    }

    public void setVersionType(Integer versionType) {
        this.versionType = versionType;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
