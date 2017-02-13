package com.talebase.cloud.base.ms.paper.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du on 2016-12-9.
 */
public class DPaper3Request {

    /**
     * 试卷选项
     *
     */
    List<DOption> dOptions = new ArrayList<>() ;


    /**
     * 试卷上传题
     *
     */
    List<DAttachment> dAttachments= new ArrayList<>() ;

    /**
     * 试卷填空
     *
     */
    List<DBlank> dBlanks= new ArrayList<>() ;

    /**
     * 试卷总分
     */
    BigDecimal score ;

    public List<DOption> getdOptions() {
        return dOptions;
    }

    public void setdOptions(List<DOption> dOptions) {
        this.dOptions = dOptions;
    }
    public void setdOption(DOption dOption) {
        this.dOptions.add(dOption);
    }

    public List<DAttachment> getdAttachments() {
        return dAttachments;
    }

    public void setdAttachments(List<DAttachment> dAttachments) {
        this.dAttachments = dAttachments;
    }

    public void setdAttachment(DAttachment dAttachment) {
        this.dAttachments.add(dAttachment);
    }
    public List<DBlank> getdBlanks() {
        return dBlanks;
    }

    public void setdBlanks(List<DBlank> dBlanks) {
        this.dBlanks = dBlanks;
    }
    public void setdBlank(DBlank dBlank) {
        this.dBlanks.add(dBlank);
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
