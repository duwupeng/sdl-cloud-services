package com.talebase.cloud.base.ms.examer.domain;
  /**
  * @author auto-tool
  */public class TUserImportRecord {
   /**
   * 
   */
   Integer id;

   /**
   * 导入所属批次号
   */
   String batchNo;

   /**
   * 考生号
   */
   String examinee;

   /**
   * 导入状态,0:失败(fail);1:成功(success)
   */
   Integer status;

   /**
   * 备注
   */
   String remark;

   /**
   * 失败才要保存；xls的字段内容；主要用于导出
   */
   String detailJson;

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

   public String getExaminee() {
    return examinee;
   }

   public void setExaminee(String examinee) {
    this.examinee = examinee;
   }

   public Integer getStatus() {
    return status;
   }

   public void setStatus(Integer status) {
    this.status = status;
   }

   public String getRemark() {
    return remark;
   }

   public void setRemark(String remark) {
    this.remark = remark;
   }

   public String getDetailJson() {
    return detailJson;
   }

   public void setDetailJson(String detailJson) {
    this.detailJson = detailJson;
   }
  }
