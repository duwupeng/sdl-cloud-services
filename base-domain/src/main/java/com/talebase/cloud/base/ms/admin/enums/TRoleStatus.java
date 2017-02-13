package com.talebase.cloud.base.ms.admin.enums;
/**
*角色状态
**/
public enum TRoleStatus{
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
   private TRoleStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
