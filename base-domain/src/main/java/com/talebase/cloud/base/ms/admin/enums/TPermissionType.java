package com.talebase.cloud.base.ms.admin.enums;
/**
*类型
**/
public enum TPermissionType{
   /**
   *全局
   **/
   GLOBAL(0),

   /**
   *项目
   **/
   PROJECT(1),

   /**
   *管理员
   **/
   ADMINISTRATOR(2),

   /**
   *考试
   **/
   EXAMINATION(3);
   private final int value;
   private TPermissionType(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
