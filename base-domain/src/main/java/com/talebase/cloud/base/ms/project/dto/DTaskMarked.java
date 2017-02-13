package com.talebase.cloud.base.ms.project.dto;

/**
 * Created by kanghong.zhao on 2016-12-19.
 */
public class DTaskMarked {

    private Integer totalTaskNum;//总任务数
    private Integer totalPaperNum;//总试卷数
    private Integer doneTaskNum;//已完成任务数
    private Integer donePaperNum;//已完成试卷数

    public Integer getTotalTaskNum() {
        return totalTaskNum;
    }

    public void setTotalTaskNum(Integer totalTaskNum) {
        this.totalTaskNum = totalTaskNum;
    }

    public Integer getTotalPaperNum() {
        return totalPaperNum;
    }

    public void setTotalPaperNum(Integer totalPaperNum) {
        this.totalPaperNum = totalPaperNum;
    }

    public Integer getDoneTaskNum() {
        return doneTaskNum;
    }

    public void setDoneTaskNum(Integer doneTaskNum) {
        this.doneTaskNum = doneTaskNum;
    }

    public Integer getDonePaperNum() {
        return donePaperNum;
    }

    public void setDonePaperNum(Integer donePaperNum) {
        this.donePaperNum = donePaperNum;
    }
}
