package com.talebase.cloud.base.ms.admin.enums;
/**
*公司状态
**/
public enum TCompanyStatus{
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
   private TCompanyStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
