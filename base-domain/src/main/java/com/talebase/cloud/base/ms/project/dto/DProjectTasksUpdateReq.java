package com.talebase.cloud.base.ms.project.dto;

import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class DProjectTasksUpdateReq {

    private Integer id;
    private String name;
    private Integer paperId;
    private String startDateStr;
    private String endDateStr;
    private String latestStartDateStr;
    private Integer examTime;
    private Integer finishType;
    private Integer pageChangeLimit;
    private Integer delayLimitTime;

    private List<String> examinersArr;

    private List<TTaskExaminer> examiners = new ArrayList<>();

    //这4个字段是在os中组装出来的，只能create
    private Integer needMarkingNum;
    private String paperUnicode;
    private BigDecimal paperVersion;
    private Integer paperNum;

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

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getLatestStartDateStr() {
        return latestStartDateStr;
    }

    public void setLatestStartDateStr(String latestStartDateStr) {
        this.latestStartDateStr = latestStartDateStr;
    }

    public Integer getExamTime() {
        return examTime;
    }

    public void setExamTime(Integer examTime) {
        this.examTime = examTime;
    }

    public Integer getFinishType() {
        return finishType;
    }

    public void setFinishType(Integer finishType) {
        this.finishType = finishType;
    }

    public Integer getPageChangeLimit() {
        return pageChangeLimit;
    }

    public void setPageChangeLimit(Integer pageChangeLimit) {
        this.pageChangeLimit = pageChangeLimit;
    }

    public Integer getDelayLimitTime() {
        return delayLimitTime;
    }

    public void setDelayLimitTime(Integer delayLimitTime) {
        this.delayLimitTime = delayLimitTime;
    }

    public List<TTaskExaminer> getExaminers() {
        return examiners;
    }

    public void setExaminers(List<TTaskExaminer> examiners) {
        this.examiners = examiners;
    }

    public Integer getNeedMarkingNum() {
        return needMarkingNum;
    }

    public void setNeedMarkingNum(Integer needMarkingNum) {
        this.needMarkingNum = needMarkingNum;
    }

    public String getPaperUnicode() {
        return paperUnicode;
    }

    public void setPaperUnicode(String paperUnicode) {
        this.paperUnicode = paperUnicode;
    }

    public BigDecimal getPaperVersion() {
        return paperVersion;
    }

    public void setPaperVersion(BigDecimal paperVersion) {
        this.paperVersion = paperVersion;
    }

    public Integer getPaperNum() {
        return paperNum;
    }

    public void setPaperNum(Integer paperNum) {
        this.paperNum = paperNum;
    }

    public List<String> getExaminersArr() {
        return examinersArr;
    }

    public void setExaminersArr(List<String> examinersArr) {
        this.examinersArr = examinersArr;
    }
}
