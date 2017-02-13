package com.talebase.cloud.base.ms.admin.enums;
/**
*角色映射状态
**/
public enum TRoleAdminStatus{
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
   private TRoleAdminStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
