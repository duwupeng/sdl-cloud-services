package com.talebase.cloud.base.ms.examer.enums;
/**
*是否唯一状态
**/
public enum TUserShowFieldIsunique{
   /**
    *唯一
    **/
   UNIQUE(1),
   /**
   *非唯一
   **/
   UNUNIQUE(0);
   private final int value;
   private TUserShowFieldIsunique(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
