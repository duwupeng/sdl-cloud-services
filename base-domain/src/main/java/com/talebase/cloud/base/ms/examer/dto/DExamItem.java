package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.paper.dto.DOptionItem;

import java.util.List;

/**
 * Created by eric.du on 2016-12-12.
 */
public class DExamItem {

    Integer id;
    /**
     *4. 单选
     *5. 多选
     *6. 填空
     *7. 附件
     */
    Integer type;
    /**
     * 题目编号
     */
    int  seqNo;

    /**
     * 题前说明
     */
    List instructionPre;

    /**
     * 题后说明
     */
    List instructionPos;

    /**
     * 试卷题目
     */
    String question;

    /**
     * 填空题类型，0,主观;1,客观
     */
    int blankType;
    /**
     * 填空题字数上限
     */
    String blankLimit;
    /**
     *选项内容
     */
    List<DOptionItem> dOptionItems;

    /**
     * 附件类型
     */
    String aTypes;

    /**
     *所选答案
     * seqNo. 当前题目的Id
     * 1. 选择题 ：[{"seqNo":1,"answers":["A"],"type":4}]
     * 2. 多选题： [{"seqNo":1,"answers":["A","B"],"type":5}]
     * 3. 填空题：[{"seqNo":1,"answers":["填空答案1","填空答案2"],"type":6}]
     * 4. 上传题目[{"seqNo":1,"answers":["附件path1","附件path2"],"type":7}]
     * @return
     */
    String[] answers;

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public int getBlankType() {
        return blankType;
    }

    public void setBlankType(int blankType) {
        this.blankType = blankType;
    }

    public List<DOptionItem> getdOptionItems() {
        return dOptionItems;
    }

    public void setdOptionItems(List<DOptionItem> dOptionItems) {
        this.dOptionItems = dOptionItems;
    }

    public String getaTypes() {
        return aTypes;
    }

    public void setInstructionPre(List instructionPre) {
        this.instructionPre = instructionPre;
    }

    public List getInstructionPos() {
        return instructionPos;
    }

    public void setInstructionPos(List instructionPos) {
        this.instructionPos = instructionPos;
    }

    public List getInstructionPre() {
        return instructionPre;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public void setaTypes(String aTypes) {
        this.aTypes = aTypes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBlankLimit() {
        return blankLimit;
    }

    public void setBlankLimit(String blankLimit) {
        this.blankLimit = blankLimit;
    }
}
