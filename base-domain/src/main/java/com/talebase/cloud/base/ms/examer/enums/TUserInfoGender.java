package com.talebase.cloud.base.ms.examer.enums;
/**
*性别
**/
public enum TUserInfoGender{
   /**
   *女
   **/
   FEMALE(0),

   /**
   *男
   **/
   MALE(1),

   /**
   *其他
   **/
   OTHER(2);
   private final int value;
   private TUserInfoGender(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
