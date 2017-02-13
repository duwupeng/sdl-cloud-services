package com.talebase.cloud.base.ms.examer.domain;

import java.sql.Timestamp;

/**
  * @author auto-tool
  */public class TUserImportLog {
   /**
   * 
   */
   Integer id;

   /**
   * 导入批次号
   */
   String batchNo;

   /**
   * 导入状态,0:进行中(OnDoing);1:已完成(Done);2:解析失败(Fail)
   */
   Integer status;

   /**
   * 成功导入记录
   */
   Integer succNum;

   /**
   * 失败记录
   */
   Integer failNum;

   /**
   * 操作管理员账号
   */
   String creater;

   /**
   * 开始导入时间
   */
   java.sql.Timestamp createdDate;

   /**
   * 完成导入时间
   */
   java.sql.Timestamp finishedDate;

   /**
   * 相关项目id
   */
   Integer projectId;

   /**
   * 相关任务id
   */
   Integer taskId;

   /**
   * 
   */
   Integer companyId;

   /**
   * 表单头
   */
   String titleJson;

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public String getBatchNo() {
  return batchNo;
 }

 public void setBatchNo(String batchNo) {
  this.batchNo = batchNo;
 }

 public Integer getStatus() {
  return status;
 }

 public void setStatus(Integer status) {
  this.status = status;
 }

 public Integer getSuccNum() {
  return succNum;
 }

 public void setSuccNum(Integer succNum) {
  this.succNum = succNum;
 }

 public Integer getFailNum() {
  return failNum;
 }

 public void setFailNum(Integer failNum) {
  this.failNum = failNum;
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

 public Timestamp getFinishedDate() {
  return finishedDate;
 }

 public void setFinishedDate(Timestamp finishedDate) {
  this.finishedDate = finishedDate;
 }

 public Integer getProjectId() {
  return projectId;
 }

 public void setProjectId(Integer projectId) {
  this.projectId = projectId;
 }

 public Integer getTaskId() {
  return taskId;
 }

 public void setTaskId(Integer taskId) {
  this.taskId = taskId;
 }

 public Integer getCompanyId() {
  return companyId;
 }

 public void setCompanyId(Integer companyId) {
  this.companyId = companyId;
 }

 public String getTitleJson() {
  return titleJson;
 }

 public void setTitleJson(String titleJson) {
  this.titleJson = titleJson;
 }
}
