package com.talebase.cloud.base.ms.common.enumes;
/**
*发送状态
**/
public enum TEmailLogStatus{
   /**
   *失败
   **/
   FAILURE(0),

   /**
   *成功
   **/
   SUCCESS(1);
   private final int value;
   private TEmailLogStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
