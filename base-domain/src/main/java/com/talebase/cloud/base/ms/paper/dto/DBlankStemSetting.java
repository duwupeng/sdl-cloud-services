package com.talebase.cloud.base.ms.paper.dto;

import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DBlankStemSetting {

    /**
     * 题干
     */
    String question;

    /**
     * 填空个数
     */
    Integer numbers;

    /**
     * 题型,0:主观(subjective );1:客观(objective)
     */
    Integer type;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBlankStemSetting dBlankStemSetting = (DBlankStemSetting) o;

        if (type != null && dBlankStemSetting.type != null && !type.equals(dBlankStemSetting.type))
            return false;
        if (numbers != null && dBlankStemSetting.numbers != null && !numbers.equals(dBlankStemSetting.numbers))
            return false;
        return question.equals(dBlankStemSetting.question);
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + numbers.hashCode();
        return result;
    }
}
