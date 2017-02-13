package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;

/**
 * Created by eric.du on 2016-12-9.
 */
public class DPaperComposer {
    Integer type;

    Integer subjectId;

    String  unicCode;

    Integer seqNo;

    BigDecimal score;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getUnicCode() {
        return unicCode;
    }

    public void setUnicCode(String unicCode) {
        this.unicCode = unicCode;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DPaperComposer that = (DPaperComposer) o;

        if (!type.equals(that.type)) return false;
//        if (!subjectId.equals(that.subjectId)) return false;
        if (!unicCode.equals(that.unicCode)) return false;
//        if (!seqNo.equals(that.seqNo)) return false;
        return score.compareTo(that.score)==0;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
//        result = 31 * result + subjectId.hashCode();
        result = 31 * result + unicCode.hashCode();
//        result = 31 * result + seqNo.hashCode();
        result = 31 * result + score.hashCode();
        return result;
    }
}
