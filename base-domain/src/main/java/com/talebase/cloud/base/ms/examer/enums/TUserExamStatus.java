package com.talebase.cloud.base.ms.examer.enums;
/**
*测试状态
**/
public enum TUserExamStatus{
   /**
   *删除
   **/
   DELETE(-1),

   /**
   *禁用
   **/
   DISABLED(0),

   /**
   *启用
   **/
   ENABLED(1);
   private final int value;
   private TUserExamStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
