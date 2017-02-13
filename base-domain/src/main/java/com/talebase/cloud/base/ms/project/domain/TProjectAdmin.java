package com.talebase.cloud.base.ms.project.domain;
  /**
  * @author auto-tool
  */public class TProjectAdmin {
   /**
   * 
   */
   Integer id;

   /**
   * 
   */
   Integer projectId;

   /**
   * 管理员账号
   */
   String account;

   /**
   * 管理员名称
   */
   String name;

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

   public String getAccount() {
    return account;
   }

   public void setAccount(String account) {
    this.account = account;
   }

   public String getName() {
    return name;
   }

   public void setName(String name) {
    this.name = name;
   }
  }
