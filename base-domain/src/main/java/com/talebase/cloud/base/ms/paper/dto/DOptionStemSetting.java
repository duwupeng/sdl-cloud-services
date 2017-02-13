package com.talebase.cloud.base.ms.paper.dto;

import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DOptionStemSetting {

    /**
     * 选择题题干
     */
    String question;

    /**
     * 选项列表
     */
    List<DOptionItem> options;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<DOptionItem> getOptions() {
        return options;
    }

    public void setOptions(List<DOptionItem> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DOptionStemSetting dOptionStemSetting = (DOptionStemSetting) o;

        if (options != null && dOptionStemSetting.options != null && !options.equals(dOptionStemSetting.options))
            return false;
        return question.equals(dOptionStemSetting.question);
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + options.hashCode();
        return result;
    }
}
