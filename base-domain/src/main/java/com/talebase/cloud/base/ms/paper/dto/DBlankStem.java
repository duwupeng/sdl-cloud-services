package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DBlankStem {

    Integer seqNo;
    /**
     * 问题， 填空题整题内容,  填空处以?作为填充标识
     */
    String question;

    /**
     * 填空个数
     */
    Integer numbers;

    /**
     * 1. 试卷信息
     * 2. 页码
     * 3. 说明
     * 4. 单择题
     * 5. 多择题
     * 6. 填空题
     * 7. 上传题目
     * 8. 结束语
     */
    Integer type;
    /**
     * 填空题样式
     */
    DBlankStyleSetting dBlankStyleSetting;

    DInstruction dInstruction;

    /**
     * 填空题题型,0:主观(subjective );1:客观(objective)
     */
    Integer blankType;

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

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public DBlankStyleSetting getdBlankStyleSetting() {
        return dBlankStyleSetting;
    }

    public void setdBlankStyleSetting(DBlankStyleSetting dBlankStyleSetting) {
        this.dBlankStyleSetting = dBlankStyleSetting;
    }

    public DInstruction getdInstruction() {
        return dInstruction;
    }

    public void setdInstruction(DInstruction dInstruction) {
        this.dInstruction = dInstruction;
    }

    public Integer getBlankType() {
        return blankType;
    }

    public void setBlankType(Integer blankType) {
        this.blankType = blankType;
    }
}
