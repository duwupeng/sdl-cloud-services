package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.paper.dto.DBlankStyleSetting;

import java.util.List;
import java.util.Map;

/**
 * Created by daorong.li on 2016-12-21.
 */
public class DScoreSubjectResp {
    private Integer number;//试题序号
    private String stem;//题干
    private String referenceAnswer;//评分标准
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
    private Integer type;
    /**
     * 本题附件地址
     */
    //List<String> filePath;

    /**
     * 考生答案
     */
    //String[]  answers;//考生答案
    List<Map<String,Object>> answers;

    List<String> fullScore;//题满分

    List<String> score;//得分

    boolean canScore;//能不能打分

    /**
     * 填空题宽、高
     */
    DBlankStyleSetting dBlankStyleSetting;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

//    public List<String> getFilePath() {
//        return filePath;
//    }
//
//    public void setFilePath(List<String> filePath) {
//        this.filePath = filePath;
//    }

    public List<Map<String, Object>> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Map<String, Object>> answers) {
        this.answers = answers;
    }

    public List<String> getFullScore() {
        return fullScore;
    }

    public void setFullScore(List<String> fullScore) {
        this.fullScore = fullScore;
    }

    public List<String> getScore() {
        return score;
    }

    public void setScore(List<String> score) {
        this.score = score;
    }

    public boolean isCanScore() {
        return canScore;
    }

    public void setCanScore(boolean canScore) {
        this.canScore = canScore;
    }

    public DBlankStyleSetting getdBlankStyleSetting() {
        return dBlankStyleSetting;
    }

    public void setdBlankStyleSetting(DBlankStyleSetting dBlankStyleSetting) {
        this.dBlankStyleSetting = dBlankStyleSetting;
    }
}
