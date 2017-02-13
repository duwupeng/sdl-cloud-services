package com.talebase.cloud.base.ms.admin.enums;
/**
*用户状态
**/
public enum TAdminStatus{
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
   private TAdminStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
