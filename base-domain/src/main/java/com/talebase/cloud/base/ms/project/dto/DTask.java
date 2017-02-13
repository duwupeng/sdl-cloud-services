package com.talebase.cloud.base.ms.project.dto;

import java.math.BigDecimal;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class DTask {

    private Integer id;
    private String name;
    private Integer projectId;

    private Integer status;
    //    private Date startDate;//任务开始时间
//    private Date endDate;//任务结束时间
    private Long startDateL;//任务开始时间
    private Long endDateL;//任务结束时间
    private Long latestStartDateL;

    private Integer preNum;//预考人数
    private Integer inNum;//开考人数
    private Integer finishNum;//交卷人数
    private Integer markedNum;//已评卷人数

    private Integer paperId;//试卷id
    private String paperUnicode;//试卷编号
    private BigDecimal paperVersion;//试卷版本
    private Boolean hasUpgradeVersion = false;//是否存在更新版本
    private Integer needMarkingNum = 0;//主观题数目

    public Boolean getHasUpgradeVersion() {
        return hasUpgradeVersion;
    }

    public void setHasUpgradeVersion(Boolean hasUpgradeVersion) {
        this.hasUpgradeVersion = hasUpgradeVersion;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLatestStartDateL() {
        return latestStartDateL;
    }

    public void setLatestStartDateL(Long latestStartDateL) {
        this.latestStartDateL = latestStartDateL;
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

    public Integer getPreNum() {
        return preNum;
    }

    public void setPreNum(Integer preNum) {
        this.preNum = preNum;
    }

    public Integer getInNum() {
        return inNum;
    }

    public void setInNum(Integer inNum) {
        this.inNum = inNum;
    }

    public Integer getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(Integer finishNum) {
        this.finishNum = finishNum;
    }

    public Integer getMarkedNum() {
        return markedNum;
    }

    public void setMarkedNum(Integer markedNum) {
        this.markedNum = markedNum;
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

    public Integer getNeedMarkingNum() {
        return needMarkingNum;
    }

    public void setNeedMarkingNum(Integer needMarkingNum) {
        this.needMarkingNum = needMarkingNum;
    }
}
