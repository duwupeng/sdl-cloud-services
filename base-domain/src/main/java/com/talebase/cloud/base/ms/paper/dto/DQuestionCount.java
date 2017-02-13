package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-12.
 */
public class DQuestionCount {
    List<DUnicode> unicodeList;
    BigDecimal totalScore = new BigDecimal(0);
    Integer subjectiveCount = 0;
    Integer objectiveCount = 0;
    Integer totalCount = 0;
    boolean hasChange=false;

    public List<DUnicode> getUnicodeList() {
        return unicodeList;
    }

    public void setUnicodeList(List<DUnicode> unicodeList) {
        this.unicodeList = unicodeList;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getSubjectiveCount() {
        return subjectiveCount;
    }

    public void setSubjectiveCount(Integer subjectiveCount) {
        this.subjectiveCount = subjectiveCount;
    }

    public Integer getObjectiveCount() {
        return objectiveCount;
    }

    public void setObjectiveCount(Integer objectiveCount) {
        this.objectiveCount = objectiveCount;
    }

    public Integer getTotalCount() {
        return subjectiveCount + objectiveCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isHasChange() {
        return hasChange;
    }

    public void setHasChange(boolean hasChange) {
        this.hasChange = hasChange;
    }
}
