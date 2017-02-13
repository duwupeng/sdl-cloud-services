package com.talebase.cloud.base.ms.paper.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DAttachmentStemSetting {

    /**
     * 问题
     */
    String question;
    /**
     * 附件
     */
    /**
     * 上传题目类型,0:图片(pic);1:文档(word);2:工作表(excel)
     */
    String type;


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DAttachmentStemSetting dAttachmentStemSetting = (DAttachmentStemSetting) o;

        if (type != null && dAttachmentStemSetting.type != null && !type.equals(dAttachmentStemSetting.type))
            return false;
        return question.equals(dAttachmentStemSetting.question);
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
