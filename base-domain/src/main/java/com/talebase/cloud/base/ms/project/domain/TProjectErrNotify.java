package com.talebase.cloud.base.ms.project.domain;
  /**
  * @author auto-tool
  */public class TProjectErrNotify {
   /**
   * 
   */
   Integer id;

   /**
   * 项目id
   */
   Integer projectId;

   /**
   * 待告知发送失败条数
   */
   Integer errNum;

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

   public Integer getErrNum() {
    return errNum;
   }

   public void setErrNum(Integer errNum) {
    this.errNum = errNum;
   }
  }
