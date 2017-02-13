package com.talebase.cloud.base.ms.project.dto;

import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class DTaskInEdit {

    private Integer id;
    private String name;
    private Integer finishType;
    private Long startDateL;
    private Long endDateL;
    private Long latestStartDateL;
    private Integer examTime;
    private Integer pageChangeLimit;
    private List<DTaskExamier> examiners = new ArrayList<>();
    private Integer paperId;

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFinishType() {
        return finishType;
    }

    public void setFinishType(Integer finishType) {
        this.finishType = finishType;
    }

    public Long getStartDateL() {
        return startDateL;
    }

    public void setStartDateL(Long startDateL) {
        this.startDateL = startDateL;
    }

    public Long getEndDateL() {
        return endDateL;
    }

    public void setEndDateL(Long endDateL) {
        this.endDateL = endDateL;
    }

    public Long getLatestStartDateL() {
        return latestStartDateL;
    }

    public void setLatestStartDateL(Long latestStartDateL) {
        this.latestStartDateL = latestStartDateL;
    }

    public Integer getExamTime() {
        return examTime;
    }

    public void setExamTime(Integer examTime) {
        this.examTime = examTime;
    }

    public Integer getPageChangeLimit() {
        return pageChangeLimit;
    }

    public void setPageChangeLimit(Integer pageChangeLimit) {
        this.pageChangeLimit = pageChangeLimit;
    }

    public List<DTaskExamier> getExaminers() {
        return examiners;
    }

    public void setExaminers(List<DTaskExamier> examiners) {
        this.examiners = examiners;
    }
}
