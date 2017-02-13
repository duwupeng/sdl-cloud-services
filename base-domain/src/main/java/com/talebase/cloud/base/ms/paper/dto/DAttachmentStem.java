package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DAttachmentStem {

    DInstruction dInstruction;
    Integer seqNo;
    /**
     * 问题
     */
    String question;

    /**
     * 题目类型
     */
    Integer type;

    public DInstruction getdInstruction() {
        return dInstruction;
    }

    public void setdInstruction(DInstruction dInstruction) {
        this.dInstruction = dInstruction;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

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
}
