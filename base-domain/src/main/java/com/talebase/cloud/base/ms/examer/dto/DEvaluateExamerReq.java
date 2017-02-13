package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.paper.dto.DItem;

/**
 * Created by eric.du on 2016-12-19.
 */
public class DEvaluateExamerReq extends DItem{
    Integer examerId;
    Integer score;

    public Integer getExamerId() {
        return examerId;
    }

    public void setExamerId(Integer examerId) {
        this.examerId = examerId;
    }

//    public Integer getScore() {
//        return score;
//    }
//
//    public void setScore(Integer score) {
//        this.score = score;
//    }
}
