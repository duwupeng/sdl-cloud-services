package com.talebase.cloud.base.ms.examer.enums;
/**
*导入状态
**/
public enum TUserImportRecordStatus{
   /**
   *失败
   **/
   FAIL(0),

   /**
   *成功
   **/
   SUCCESS(1);
   private final int value;
   private TUserImportRecordStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
