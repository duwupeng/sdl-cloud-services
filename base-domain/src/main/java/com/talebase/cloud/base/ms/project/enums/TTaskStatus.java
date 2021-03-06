package com.talebase.cloud.base.ms.project.enums;
/**
*项目状态
**/
public enum TTaskStatus{
   /**
   *禁用
   **/
   DISABLE(0),

   /**
   *启用
   **/
   ENABLE(1),

   /**
   *删除
   **/
   DELETE(2);
   private final int value;
   private TTaskStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
