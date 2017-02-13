package com.talebase.cloud.base.ms.project.domain;

import java.sql.Timestamp;

/**
  * @author auto-tool
  */public class TProject {
   /**
   * 
   */
   Integer id;

   /**
   * 项目名称(企业惟一)
   */
   String name;

   /**
   * 项目说明
   */
   String description;

   /**
   * 所属公司id
   */
   Integer companyId;

   /**
   * 创建人
   */
   String creater;

   /**
   * 创建时间
   */
   java.sql.Timestamp createdDate;

   /**
   * 修改人
   */
   String modifier;

   /**
   * 修改时间
   */
   java.sql.Timestamp modifiedDate;

   /**
   * 项目状态,0:禁用(disable);1:启用(enable);2:删除(delete)
   */
   Integer status;

   /**
   * 项目开始时间
   */
   java.sql.Timestamp startDate;

   /**
   * 项目结束时间
   */
   java.sql.Timestamp endDate;

   /**
   * 扫码人数上限
   */
   Integer scanMax;

   /**
   * 当前扫码人数
   */
   Integer scanNow;

   /**
   * 扫码生成账号前缀
   */
   String scanAccountPre;

   /**
   * 扫码有效日期-开始
   */
   java.sql.Timestamp scanStartDate;

   /**
   * 扫码有效日期-结束
   */
   java.sql.Timestamp scanEndDate;

   /**
   * 是否开启扫码,0:否(disable);1:开启(enable)
   */
   Integer scanEnable;

    /**
     *二维码base64字符串
     */
   String scanImage;


    public String getScanImage() {
        return scanImage;
    }

    public void setScanImage(String scanImage) {
        this.scanImage = scanImage;
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

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public Integer getCompanyId() {
  return companyId;
 }

 public void setCompanyId(Integer companyId) {
  this.companyId = companyId;
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

 public Timestamp getStartDate() {
  return startDate;
 }

 public void setStartDate(Timestamp startDate) {
  this.startDate = startDate;
 }

 public Timestamp getEndDate() {
  return endDate;
 }

 public void setEndDate(Timestamp endDate) {
  this.endDate = endDate;
 }

 public Integer getScanMax() {
  return scanMax;
 }

 public void setScanMax(Integer scanMax) {
  this.scanMax = scanMax;
 }

 public Integer getScanNow() {
  return scanNow;
 }

 public void setScanNow(Integer scanNow) {
  this.scanNow = scanNow;
 }

 public String getScanAccountPre() {
  return scanAccountPre;
 }

 public void setScanAccountPre(String scanAccountPre) {
  this.scanAccountPre = scanAccountPre;
 }

 public Timestamp getScanStartDate() {
  return scanStartDate;
 }

 public void setScanStartDate(Timestamp scanStartDate) {
  this.scanStartDate = scanStartDate;
 }

 public Timestamp getScanEndDate() {
  return scanEndDate;
 }

 public void setScanEndDate(Timestamp scanEndDate) {
  this.scanEndDate = scanEndDate;
 }

 public Integer getScanEnable() {
  return scanEnable;
 }

 public void setScanEnable(Integer scanEnable) {
  this.scanEnable = scanEnable;
 }
}
