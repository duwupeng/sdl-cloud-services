package com.talebase.cloud.base.ms.common.domain;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */public class TCode {
 /**
  *
  */
 Integer id;

 /**
  * 代码值
  */
 String value;

 /**
  * 类型
  */
 String type;

 /**
  * 名称
  */
 String name;

 /**
  * 状态,0:禁用(disabled);1:启用(Enabled)
  */
 Integer status;

 /**
  *
  */
 java.sql.Timestamp createDate;

 /**
  *
  */
 String creater;

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public String getValue() {
  return value;
 }

 public void setValue(String value) {
  this.value = value;
 }

 public String getType() {
  return type;
 }

 public void setType(String type) {
  this.type = type;
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

 public Timestamp getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Timestamp createDate) {
  this.createDate = createDate;
 }

 public String getCreater() {
  return creater;
 }

 public void setCreater(String creater) {
  this.creater = creater;
 }
}
