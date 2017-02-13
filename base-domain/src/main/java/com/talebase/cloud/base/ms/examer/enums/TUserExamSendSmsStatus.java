package com.talebase.cloud.base.ms.examer.enums;
/**
*短信发送状态
**/
public enum TUserExamSendSmsStatus{
   /**
   *发送中
   **/
   SMS_SENDING(1),

   /**
   *发送失败
   **/
   SMS_FAILURE(2),

   /**
   *发送成功
   **/
   SMS_SUCCESS(3);
   private final int value;
   private TUserExamSendSmsStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
