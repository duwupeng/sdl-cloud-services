package com.talebase.cloud.base.ms.examer.enums;
/**
*发送邮件状态
**/
public enum TUserExamSendEmailStatus{
   /**
   *发送中
   **/
   EMAIL_SENDING(1),

   /**
   *发送失败
   **/
   EMAIL_FAILURE(2),

   /**
   *发送成功
   **/
   EMAIL_SUCCESS(3);
   private final int value;
   private TUserExamSendEmailStatus(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
