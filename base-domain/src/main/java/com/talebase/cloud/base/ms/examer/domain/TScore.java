package com.talebase.cloud.base.ms.examer.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author auto-tool
 */public class TScore {
 /**
  * 得分id
  */
 Integer id;

 /**
  * 序号
  */
// String serial_;
 String serialNo;

 /**
  * 试卷id
  */
 Integer paperId;

 /**
  * 得分
  */
 String score;

 /**
  *
  */
 Integer examId;

 /**
  * 创建人
  */
 String creater;

 /**
  * 创建时间
  */
 java.sql.Timestamp createTime;

 /**
  *修改人
  */
 String modify;

 /**
  *修改时间
  */
 java.sql.Timestamp modifyTime;
 /**
  * 已评填空数
  */
 Integer done;
 /**
  * 总共填空数
  */
 Integer total;

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public String getSerialNo() {
  return serialNo;
 }

 public void setSerialNo(String serialNo) {
  this.serialNo = serialNo;
 }

 public Integer getPaperId() {
  return paperId;
 }

 public void setPaperId(Integer paperId) {
  this.paperId = paperId;
 }

 public String getScore() {
  return score;
 }

 public void setScore(String score) {
  this.score = score;
 }

 public Integer getExamId() {
  return examId;
 }

 public void setExamId(Integer examId) {
  this.examId = examId;
 }

 public String getCreater() {
  return creater;
 }

 public void setCreater(String creater) {
  this.creater = creater;
 }

 public Timestamp getCreateTime() {
  return createTime;
 }

 public void setCreateTime(Timestamp createTime) {
  this.createTime = createTime;
 }

 public String getModify() {
  return modify;
 }

 public void setModify(String modify) {
  this.modify = modify;
 }

 public Timestamp getModifyTime() {
  return modifyTime;
 }

 public void setModifyTime(Timestamp modifyTime) {
  this.modifyTime = modifyTime;
 }

 public Integer getDone() {
  return done;
 }

 public void setDone(Integer done) {
  this.done = done;
 }

 public Integer getTotal() {
  return total;
 }

 public void setTotal(Integer total) {
  this.total = total;
 }


}
