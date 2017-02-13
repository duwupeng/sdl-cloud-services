package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.paper.dto.DBlankStyleSetting;
import com.talebase.cloud.base.ms.paper.dto.DItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by eric.du on 2016-12-19.
 */
public class DEvaluateItemDetail extends DItem {

    /**
     * 题目序号
     */
    int seqNo;

    /**
     * 题目题干
     */
    String stem;

    /**
     * 评分标准
     */
    String referenceAnswer;

    /**
     * 考生答案--上传题地址
     */
    List<String> filePath;
    /**
     * 考生答案--上传题地址的位置
     */
    List<String> filePathPositions;

    /**
     * 考生答案--主观填空题
     */
    //String[] answers;
    List<Map<String,Object>> answers;

    DBlankStyleSetting dBlankStyleSetting;
    /**
     * 打分空格数
     */
    Integer spaceNum;

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getReferenceAnswer() {
        return referenceAnswer;
    }

    public void setReferenceAnswer(String referenceAnswer) {
        this.referenceAnswer = referenceAnswer;
    }

    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }

    public List<Map<String, Object>> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Map<String, Object>> answers) {
        this.answers = answers;
    }

    public Integer getSpaceNum() {
        return spaceNum;
    }

    public void setSpaceNum(Integer spaceNum) {
        this.spaceNum = spaceNum;
    }

    public List<String> getFilePathPositions() {
        return filePathPositions;
    }

    public void setFilePathPositions(List<String> filePathPositions) {
        this.filePathPositions = filePathPositions;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public DBlankStyleSetting getdBlankStyleSetting() {
        return dBlankStyleSetting;
    }

    public void setdBlankStyleSetting(DBlankStyleSetting dBlankStyleSetting) {
        this.dBlankStyleSetting = dBlankStyleSetting;
    }
}
