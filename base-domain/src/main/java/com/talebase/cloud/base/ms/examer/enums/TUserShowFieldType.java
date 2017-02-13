package com.talebase.cloud.base.ms.examer.enums;
/**
*自定义类型
**/
public enum TUserShowFieldType{
   /**
   *输入框
   **/
   INPUT(1),

   /**
   *日期类型
   **/
   DATE_TYPE(2),

   /**
   *下拉框
   **/
   SELECT_TYPE(3);
   private final int value;
   private TUserShowFieldType(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
