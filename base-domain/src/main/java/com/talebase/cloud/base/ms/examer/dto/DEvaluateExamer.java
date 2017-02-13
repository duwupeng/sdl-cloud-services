package com.talebase.cloud.base.ms.examer.dto;

import java.util.List;

/**
 * Created by eric.du on 2016-12-19.
 */
public class DEvaluateExamer{
    /**
     * 考生序号
     */
    Integer seq;
    /**
     * 考生ID
     */
    Integer examerId;
    /**
     * 题目明细
     */
    List<DEvaluateItemDetail> dItemDetails;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public List<DEvaluateItemDetail> getdItemDetails() {
        return dItemDetails;
    }

    public void setdItemDetails(List<DEvaluateItemDetail> dItemDetails) {
        this.dItemDetails = dItemDetails;
    }

    public Integer getExamerId() {
        return examerId;
    }

    public void setExamerId(Integer examerId) {
        this.examerId = examerId;
    }
}
