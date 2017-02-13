package com.talebase.cloud.base.ms.examer.enums;
/**
*是否扩展字段
**/
public enum TUserShowFieldIsextension{
   /**
   *非扩展
   **/
   UNEXTENSION(0),

   /**
   *扩展
   **/
   EXTENSION(1);
   private final int value;
   private TUserShowFieldIsextension(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
