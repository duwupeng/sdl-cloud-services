package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.paper.dto.DBlankStyleSetting;
import com.talebase.cloud.base.ms.project.dto.DTaskMarked;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-20.
 */
public class DScoreSubjectMarkListResp {
    private DTaskMarked taskMarked;//任务完成情况
    private String paperName;//试卷题目
    private Integer number;//试题序号
    private String stem;//题干
    private String referenceAnswer;//评分标准
    /**
     * 1. 试卷信息
     * 2. 页码
     * 3. 说明
     * 4. 选择题
     * 5. 填空题
     * 6. 上传题目
     * 7. 结束语
     */
    private Integer type;

    private List<DExamerAnswers> examerAnswerses;

    /**
     * 填空题宽、高
     */
    DBlankStyleSetting dBlankStyleSetting;

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

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

    public List<DExamerAnswers> getExamerAnswerses() {
        return examerAnswerses;
    }

    public void setExamerAnswerses(List<DExamerAnswers> examerAnswerses) {
        this.examerAnswerses = examerAnswerses;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public DTaskMarked getTaskMarked() {
        return taskMarked;
    }

    public void setTaskMarked(DTaskMarked taskMarked) {
        this.taskMarked = taskMarked;
    }

    public DBlankStyleSetting getdBlankStyleSetting() {
        return dBlankStyleSetting;
    }

    public void setdBlankStyleSetting(DBlankStyleSetting dBlankStyleSetting) {
        this.dBlankStyleSetting = dBlankStyleSetting;
    }
}
