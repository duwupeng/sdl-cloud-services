package com.talebase.cloud.base.ms.examer.enums;
/**
*导入状态
**/
public enum TUserImportLogStatus{
   /**
   *进行中
   **/
   _ON_DOING(0),

   /**
   *已完成
   **/
   _DONE(1),

   /**
   *解析失败
   **/
   _FAIL(2);
   private final int value;
   private TUserImportLogStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
