package com.talebase.cloud.base.ms.examer.enums;
/**
*必填状态
**/
public enum TUserShowFieldIsmandatory{
   /**
   *非强制
   **/
   UNMANDATORY(0),

   /**
   *强制
   **/
   MANDATORY(1);
   private final int value;
   private TUserShowFieldIsmandatory(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
