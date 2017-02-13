package com.talebase.cloud.os.paper.vo;

import com.talebase.cloud.base.ms.paper.dto.DItem;
import com.talebase.cloud.base.ms.paper.dto.DOptionScoreSetting;
import com.talebase.cloud.base.ms.paper.dto.DOptionStemSetting;

import java.math.BigDecimal;

/**
 * Created by eric.du on 2016-12-6.
 */
public class SingleOption {
    private String index;
    private String type;
    private String title;
    private String[] options;
    private String answer;
    private BigDecimal score;

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

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
