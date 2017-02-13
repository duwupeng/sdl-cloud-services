package com.talebase.cloud.base.ms.project.domain;

import java.sql.Timestamp;

/**
  * @author auto-tool
  */public class TTask {
   /**
   * 
   */
   Integer id;

   /**
   * 项目id
   */
   Integer projectId;

   /**
   * 公司id
   */
   Integer companyId;

   /**
   * 任务名称
   */
   String name;

   /**
   * 
   */
   String creater;

   /**
   * 
   */
   java.sql.Timestamp createdDate;

   /**
   * 
   */
   String modifier;

   /**
   * 
   */
   java.sql.Timestamp modifiedDate;

   /**
   * 项目状态,0:禁用(disable);1:启用(enable);2:删除(delete)
   */
   Integer status;

   /**
   * 页面切换次数上限;默认为null不限制
   */
   Integer pageChangeLimit;

   /**
   * 对应试卷id
   */
   Integer paperId;

   /**
   * 需要阅卷题数，默认为0，即不需要阅卷
   */
   Integer needMarkingNum;

   /**
   * 开考时间
   */
   java.sql.Timestamp startDate;

   /**
   * 最晚开考时间
   */
   java.sql.Timestamp latestStartDate;

   /**
   * 任务结束时间
   */
   java.sql.Timestamp endDate;

   /**
   * 考试时长(单位：分钟)
   */
   Integer examTime;
    /**
     * 试卷编号
     */
    String paperUnicode;

    /**
     * 试卷版本
     */
    Integer paperVersion;

    /**
     * 试卷总题目数
     */
    Integer paperNum;

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public Integer getProjectId() {
  return projectId;
 }

 public void setProjectId(Integer projectId) {
  this.projectId = projectId;
 }

 public Integer getCompanyId() {
  return companyId;
 }

 public void setCompanyId(Integer companyId) {
  this.companyId = companyId;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getCreater() {
  return creater;
 }

 public void setCreater(String creater) {
  this.creater = creater;
 }

 public Timestamp getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Timestamp createdDate) {
  this.createdDate = createdDate;
 }

 public String getModifier() {
  return modifier;
 }

 public void setModifier(String modifier) {
  this.modifier = modifier;
 }

 public Timestamp getModifiedDate() {
  return modifiedDate;
 }

 public void setModifiedDate(Timestamp modifiedDate) {
  this.modifiedDate = modifiedDate;
 }

 public Integer getStatus() {
  return status;
 }

 public void setStatus(Integer status) {
  this.status = status;
 }

 public Integer getPageChangeLimit() {
  return pageChangeLimit;
 }

 public void setPageChangeLimit(Integer pageChangeLimit) {
  this.pageChangeLimit = pageChangeLimit;
 }

 public Integer getPaperId() {
  return paperId;
 }

 public void setPaperId(Integer paperId) {
  this.paperId = paperId;
 }

 public Integer getNeedMarkingNum() {
  return needMarkingNum;
 }

 public void setNeedMarkingNum(Integer needMarkingNum) {
  this.needMarkingNum = needMarkingNum;
 }

 public Timestamp getStartDate() {
  return startDate;
 }

 public void setStartDate(Timestamp startDate) {
  this.startDate = startDate;
 }

 public Timestamp getLatestStartDate() {
  return latestStartDate;
 }

 public void setLatestStartDate(Timestamp latestStartDate) {
  this.latestStartDate = latestStartDate;
 }

 public Timestamp getEndDate() {
  return endDate;
 }

 public void setEndDate(Timestamp endDate) {
  this.endDate = endDate;
 }

 public Integer getExamTime() {
  return examTime;
 }

 public void setExamTime(Integer examTime) {
  this.examTime = examTime;
 }

    public String getPaperUnicode() {
        return paperUnicode;
    }

    public void setPaperUnicode(String paperUnicode) {
        this.paperUnicode = paperUnicode;
    }

    public Integer getPaperVersion() {
        return paperVersion;
    }

    public void setPaperVersion(Integer paperVersion) {
        this.paperVersion = paperVersion;
    }

    public Integer getPaperNum() {
        return paperNum;
    }

    public void setPaperNum(Integer paperNum) {
        this.paperNum = paperNum;
    }
}
