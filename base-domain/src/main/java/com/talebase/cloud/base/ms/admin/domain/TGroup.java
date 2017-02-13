package com.talebase.cloud.base.ms.admin.domain;

import java.sql.Timestamp;

/**
  * @author auto-tool
  */public class TGroup {
   /**
   * 分组id
   */
   Integer id;

   /**
   * 分组父id
   */
   Integer parentId;

   /**
   * 分组名称
   */
   String name;

   /**
   * 分组状态,0:无效(invalid);1:有效(effective);-1:删除(deleted)
   */
   Integer status;

   /**
   * 创建时间
   */
   java.sql.Timestamp createdDate;

   /**
   * 创建人
   */
   String creater;

   /**
   * 修改时间
   */
   java.sql.Timestamp modifiedDate;

   /**
   * 分组修改人
   */
   String modifier;

   /**
   * 权限代码
   */
   String orgCode;

   /**
   * 企业id
   */
   Integer companyId;

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public Integer getParentId() {
  return parentId;
 }

 public void setParentId(Integer parentId) {
  this.parentId = parentId;
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

 public Timestamp getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Timestamp createdDate) {
  this.createdDate = createdDate;
 }

 public String getCreater() {
  return creater;
 }

 public void setCreater(String creater) {
  this.creater = creater;
 }

 public Timestamp getModifiedDate() {
  return modifiedDate;
 }

 public void setModifiedDate(Timestamp modifiedDate) {
  this.modifiedDate = modifiedDate;
 }

 public String getModifier() {
  return modifier;
 }

 public void setModifier(String modifier) {
  this.modifier = modifier;
 }

 public String getOrgCode() {
  return orgCode;
 }

 public void setOrgCode(String orgCode) {
  this.orgCode = orgCode;
 }

 public Integer getCompanyId() {
  return companyId;
 }

 public void setCompanyId(Integer companyId) {
  this.companyId = companyId;
 }
}
