package com.talebase.cloud.base.ms.paper.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author auto-tool
 */
public class TOption {
    /**
     * 选项组
     */
    Integer id;

    /**
     * 选择题题干
     */
    String question;

    /**
     * 选项列表
     */
    String options;

    /**
     *
     */
    String answer;
    /**
     * 总分
     */

    java.math.BigDecimal score;

    /**
     * 部分选中分数
     */
    java.math.BigDecimal subScore = new BigDecimal(-999);

    /**
     * 计分规则,0:全部答对(all);1:部分答对统一给分(partUnity);2:部分答对平均给分(partAvg)
     */
    int scoreRule;

    /**
     * 题型,0:单选题(singleChoice);1:多选题(multipleChoice);2:判断题(true-false)
     */
    int type;

    /**
     * 样式
     */
    String style;
    /**
     * 创建时间
     */
    java.sql.Timestamp createdDate;

    /**
     * 创建者
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

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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

    public BigDecimal getSubScore() {
        return subScore;
    }

    public void setSubScore(BigDecimal subScore) {
        this.subScore = subScore;
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

        TOption tOption = (TOption) o;

        if (scoreRule != tOption.scoreRule) return false;
        if (type != tOption.type) return false;
        if (!question.equals(tOption.question)) return false;
        if (!options.equals(tOption.options)) return false;
        if (!answer.equals(tOption.answer)) return false;
        if (!(score.compareTo(tOption.score)==0)) return false;
        if (!(subScore.compareTo(tOption.subScore)==0)) return false;
        if (!style.equals(tOption.style)) return false;
        return unicode.equals(tOption.unicode);
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + options.hashCode();
        result = 31 * result + answer.hashCode();
        result = 31 * result + score.hashCode();
        result = 31 * result + subScore.hashCode();
        result = 31 * result + scoreRule;
        result = 31 * result + type;
        result = 31 * result + style.hashCode();
        result = 31 * result + unicode.hashCode();
        return result;
    }
}
