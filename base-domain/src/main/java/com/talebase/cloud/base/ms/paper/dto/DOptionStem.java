package com.talebase.cloud.base.ms.paper.dto;

import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DOptionStem {

    DInstruction dInstruction;

    /**
     * 题序
     */
    Integer seqNo;

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
     * 选择题题干
     */
    String question;

    /**
     * 选项列表
     */
    List<DOptionItem> options;

    /**
     * 样式
     */
    DOptionStyleSetting dOptionStyleSetting;

    /**
     * 单选题
     */
    DOptionItem option;

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

    public void setOptions(List<DOptionItem> options) {
        this.options = options;
    }

    public void setOption(DOptionItem option) {
        this.options.add(option);
    }

    public List<DOptionItem> getOptions() {
        return options;
    }

    public DOptionItem getOption() {
        return option;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public DInstruction getdInstruction() {
        return dInstruction;
    }

    public void setdInstruction(DInstruction dInstruction) {
        this.dInstruction = dInstruction;
    }

    public DOptionStyleSetting getdOptionStyleSetting() {
        return dOptionStyleSetting;
    }

    public void setdOptionStyleSetting(DOptionStyleSetting dOptionStyleSetting) {
        this.dOptionStyleSetting = dOptionStyleSetting;
    }
}
