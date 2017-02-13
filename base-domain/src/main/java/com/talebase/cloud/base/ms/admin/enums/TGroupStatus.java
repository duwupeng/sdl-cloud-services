package com.talebase.cloud.base.ms.admin.enums;
/**
*分组状态
**/
public enum TGroupStatus{
   /**
   *无效
   **/
   INVALID(0),

   /**
   *有效
   **/
   EFFECTIVE(1),

   /**
   *删除
   **/
   DELETED(-1);
   private final int value;
   private TGroupStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
