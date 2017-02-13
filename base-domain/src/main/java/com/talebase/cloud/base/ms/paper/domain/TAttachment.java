package com.talebase.cloud.base.ms.paper.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TAttachment {
    /**
     * 自增长ID
     */
    Integer id;

    /**
     * 问题
     */
    String question;

    /**
     * 该题分数
     */
    BigDecimal score;

    /**
     * 评分标准
     */
    String scoreRule;


    /**
     * 上传题目类型,1:图片(pic);2:文档(word);3:工作表(excel)
     */
    String type;

    /**
     * 创建人
     */
    java.sql.Timestamp createdDate;

    /**
     * 创建时间
     */
    String creator;

    /**
     *
     */
    String unicode;

    /**
     * @return
     */
    Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getScoreRule() {
        return scoreRule;
    }

    public void setScoreRule(String scoreRule) {
        this.scoreRule = scoreRule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TAttachment that = (TAttachment) o;

        if (type != that.type) return false;
        if (!question.equals(that.question)) return false;
        if (!(score.compareTo(that.score) == 0)) return false;
        if (!scoreRule.equals(that.scoreRule)) return false;
        return unicode.equals(that.unicode);
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + score.hashCode();
        result = 31 * result + scoreRule.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + unicode.hashCode();
        return result;
    }
}
