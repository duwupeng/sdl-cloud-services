package com.talebase.cloud.base.ms.project.domain;
  /**
  * @author auto-tool
  */public class TTaskExaminer {
   /**
   * 
   */
   Integer id;

   /**
   * 任务id
   */
   Integer taskId;

   /**
   * 考官账号
   */
   String examiner;

   /**
   * 考官名称
   */
   String name;

   public Integer getId() {
    return id;
   }

   public void setId(Integer id) {
    this.id = id;
   }

   public Integer getTaskId() {
    return taskId;
   }

   public void setTaskId(Integer taskId) {
    this.taskId = taskId;
   }

   public String getExaminer() {
    return examiner;
   }

   public void setExaminer(String examiner) {
    this.examiner = examiner;
   }

   public String getName() {
    return name;
   }

   public void setName(String name) {
    this.name = name;
   }
  }
