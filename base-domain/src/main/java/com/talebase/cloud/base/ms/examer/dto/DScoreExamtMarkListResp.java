package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.project.dto.DTaskMarked;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-21.
 */
public class DScoreExamtMarkListResp {
    private DTaskMarked taskMarked;//任务完成情况
    private Integer seqNo;//考生序号
    /**
     * 考生ID
     */
   //private Integer examerId;
    /**
     * 用户id
      */
   private Integer userId;

   private List<DScoreSubjectResp> subjectResps;
    /**
     * 试卷名称
      */
   private String paperName;

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    //    public Integer getExamerId() {
//        return examerId;
//    }
//
//    public void setExamerId(Integer examerId) {
//        this.examerId = examerId;
//    }

    public List<DScoreSubjectResp> getSubjectResps() {
        return subjectResps;
    }

    public void setSubjectResps(List<DScoreSubjectResp> subjectResps) {
        this.subjectResps = subjectResps;
    }

    public DTaskMarked getTaskMarked() {
        return taskMarked;
    }

    public void setTaskMarked(DTaskMarked taskMarked) {
        this.taskMarked = taskMarked;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }
}
