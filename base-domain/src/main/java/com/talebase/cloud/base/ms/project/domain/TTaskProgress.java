package com.talebase.cloud.base.ms.project.domain;
  /**
  * @author auto-tool
  */public class TTaskProgress {
   /**
   * 
   */
   Integer taskId;

   /**
   * 预考人数(考生账号已与任务关联)
   */
   Integer preNum;

   /**
   * 已进入考试人数
   */
   Integer inNum;

   /**
   * 交卷人数
   */
   Integer finishNum;

   /**
   * 已评卷人数
   */
   Integer markedNum;

   public Integer getTaskId() {
    return taskId;
   }

   public void setTaskId(Integer taskId) {
    this.taskId = taskId;
   }

   public Integer getPreNum() {
    return preNum;
   }

   public void setPreNum(Integer preNum) {
    this.preNum = preNum;
   }

   public Integer getInNum() {
    return inNum;
   }

   public void setInNum(Integer inNum) {
    this.inNum = inNum;
   }

   public Integer getFinishNum() {
    return finishNum;
   }

   public void setFinishNum(Integer finishNum) {
    this.finishNum = finishNum;
   }

   public Integer getMarkedNum() {
    return markedNum;
   }

   public void setMarkedNum(Integer markedNum) {
    this.markedNum = markedNum;
   }
  }
