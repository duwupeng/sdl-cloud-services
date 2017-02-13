package com.talebase.cloud.base.ms.paper.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TBlank {
    /**
     * 自增长ID
     */
    Integer id;

    /**
     * 题干
     */
    String question="";

    /**
     * 答案：存储答案，分数等信息.json
     */
    String answer="";

    /**
     * 题目解析
     */
    String explanation="";

    /**
     * 题目样式
     */
    String style="";

    /**
     * 总分
     */
    java.math.BigDecimal score = new BigDecimal(0);

    /**
     * 计分规则,0:完全一致(all);1:仅顺序不一致(partUnity)
     */
    int scoreRule=0;

    /**
     * 题型,0:主观(subjective );1:客观(objective)
     */
    int type;

    /**
     * 创建时间
     */
    java.sql.Timestamp createdDate;

    /**
     * 创建人
     */
    String creator;
    /**
     *
     */
    String unicode;

    /**
     * 版本
     */
    int version;


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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
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

    public int getScoreRule() {
        return scoreRule;
    }

    public void setScoreRule(int scoreRule) {
        this.scoreRule = scoreRule;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TBlank tBlank = (TBlank) o;

        if (scoreRule != tBlank.scoreRule) return false;
        if (type != tBlank.type) return false;
        if (!question.equals(tBlank.question)) return false;
        if (!answer.equals(tBlank.answer)) return false;
        if (!explanation.equals(tBlank.explanation)) return false;
        if (!style.equals(tBlank.style)) return false;
        if (!(score.compareTo(tBlank.score)==0)) return false;
        return unicode.equals(tBlank.unicode);
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + answer.hashCode();
        result = 31 * result + explanation.hashCode();
        result = 31 * result + style.hashCode();
        result = 31 * result + score.hashCode();
        result = 31 * result + scoreRule;
        result = 31 * result + type;
        result = 31 * result + unicode.hashCode();
        return result;
    }
}
