package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DOptionScoreSetting {

    /**
     * answer
     * DOptionScoreDetail 的json字符串
     */
//    List<DOptionScoreDetail> OptionScoreDetails;

    /**
     * 多选题的答案集合
     */
    String[] answers;

    /**
     * 单选题答案
     */
    String answer;

    /**
     * 总分
     */
    BigDecimal score;

    /**
     * 计分规则,0:全部答对(all);1:部分答对统一给分(partUnity);2:部分答对平均给分(partAvg)
     */
    Integer scoreRule;

    /**
     * 多选题设置的分数。  部分或者平均给分
     */
    BigDecimal subScore = new BigDecimal(-999);

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getScoreRule() {
        return scoreRule;
    }

    public void setScoreRule(Integer scoreRule) {
        this.scoreRule = scoreRule;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public BigDecimal getSubScore() {
        return subScore;
    }

    public void setSubScore(BigDecimal subScore) {
        this.subScore = subScore;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DOptionScoreSetting dOptionScoreSetting = (DOptionScoreSetting) o;

        if (answer != null && dOptionScoreSetting.answer != null && !answer.equals(dOptionScoreSetting.answer))
            return false;
        if (answers != null && dOptionScoreSetting.answers != null && !answers.equals(dOptionScoreSetting.answers))
            return false;
        if (score != null && dOptionScoreSetting.score != null && score.compareTo(dOptionScoreSetting.score) != 0)
            return false;
        if (scoreRule != null && dOptionScoreSetting.scoreRule != null && scoreRule != dOptionScoreSetting.scoreRule)
            return false;
        return subScore == dOptionScoreSetting.subScore;
    }

    @Override
    public int hashCode() {
        int result = answer.hashCode();
        result = 31 * result + answers.hashCode();
        result = 31 * result + score.hashCode();
        result = 31 * result + subScore.hashCode();
        result = 31 * result + scoreRule;
        return result;
    }
}
