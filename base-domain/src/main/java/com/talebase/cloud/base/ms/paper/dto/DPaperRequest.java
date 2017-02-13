package com.talebase.cloud.base.ms.paper.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by eric.du on 2016-12-9.
 */
public class DPaperRequest {

    /**
     * 试卷首语
     */
    DPaper dPaper;

    /**
     * 试卷页
     *
     */
    List<DPage> dPages = new ArrayList<DPage>();

    /**
     * 试卷说明
     *
     */
    List<DInstruction> dInstructions = new ArrayList<DInstruction>();

    /**
     * 试卷选项
     *
     */
    List<DOption> dOptions = new ArrayList<DOption>();


    /**
     * 试卷上传题
     *
     */
    List<DAttachment> dAttachments = new ArrayList<DAttachment>();

    /**
     * 试卷填空
     *
     */
    List<DBlank> dBlanks = new ArrayList<DBlank>();
    /**
     * 试卷结束语
     *
     */
    List<DPaperRemark> dPaperRemarks= new ArrayList<DPaperRemark>();


    /**
     * 组卷顺序
     */
    List<DPaperComposer> dPaperComposers;

    public DPaper getdPaper() {
        return dPaper;
    }

    public void setdPaper(DPaper dPaper) {
        this.dPaper = dPaper;
    }

    public List<DPage> getdPages() {
        return dPages;
    }

    public void setdPages(List<DPage> dPages) {
        this.dPages = dPages;
    }
    public void setdPage(DPage dPage) {
        this.dPages.add(dPage);
    }
    public List<DInstruction> getdInstructions() {
        return dInstructions;
    }

    public void setdInstructions(List<DInstruction> dInstructions) {
        this.dInstructions = dInstructions;
    }
    public void setdInstruction(DInstruction dInstruction) {
        this.dInstructions.add(dInstruction) ;
    }

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

    public List<DPaperRemark> getdPaperRemarks() {
        return dPaperRemarks;
    }

    public void setdPaperRemarks(List<DPaperRemark> dPaperRemarks) {
        this.dPaperRemarks = dPaperRemarks;
    }

    public void setdPaperRemark(DPaperRemark dPaperRemark) {
        this.dPaperRemarks.add(dPaperRemark);
    }

    public List<DPaperComposer> getdPaperComposers() {
        return dPaperComposers;
    }

    public void setdPaperComposers(List<DPaperComposer> dPaperComposers) {
        this.dPaperComposers = dPaperComposers;
    }


}
