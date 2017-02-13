package com.talebase.cloud.base.ms.paper.dto;

import java.util.List;

/**考试模块
 * Created by eric.du on 2016-12-9.
 */
public class DWPaperResponse {
    /**
     * 卷ID
     */
    Integer paperId;
    /**
     * 卷首
     */
    DPaper dPaper ;
    /**
     * 卷页
     */
     List<DWPage> pages;
    /**
     * 卷尾
     */
     List<DPaperRemark> dPaperRemarks;

    /**
     * 题目总数
     * @return
     */
     int total;

    public DPaper getdPaper() {
        return dPaper;
    }

    public void setdPaper(DPaper dPaper) {
        this.dPaper = dPaper;
    }

    public List<DWPage> getPages() {
        return pages;
    }

    public void setPages(List<DWPage> pages) {
        this.pages = pages;
    }

    public List<DPaperRemark> getdPaperRemarks() {
        return dPaperRemarks;
    }

    public void setdPaperRemarks(List<DPaperRemark> dPaperRemarks) {
        this.dPaperRemarks = dPaperRemarks;
    }
    public void setdPaperRemark(DPaperRemark dPaperRemarks) {
        this.dPaperRemarks.add(dPaperRemarks);
    }
    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
