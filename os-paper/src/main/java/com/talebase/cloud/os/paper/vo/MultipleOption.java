package com.talebase.cloud.os.paper.vo;

import java.math.BigDecimal;

/**
 * Created by eric.du on 2016-12-6.
 */
public class MultipleOption {
    private String type;
    private String title;
    private String index;
    private String[] options;
    private String answer;
    private BigDecimal score;
    private String scoreSet;
    private BigDecimal scoreLess;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
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

    public String getScoreSet() {
        return scoreSet;
    }

    public void setScoreSet(String scoreSet) {
        this.scoreSet = scoreSet;
    }

    public BigDecimal getScoreLess() {
        return scoreLess;
    }

    public void setScoreLess(BigDecimal scoreLess) {
        this.scoreLess = scoreLess;
    }
}
