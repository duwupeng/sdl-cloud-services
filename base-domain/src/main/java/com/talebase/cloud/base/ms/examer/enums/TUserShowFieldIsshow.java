package com.talebase.cloud.base.ms.examer.enums;
/**
*显示状态
**/
public enum TUserShowFieldIsshow{
   /**
   *隐藏
   **/
   HIDDEN(0),

   /**
   *显示
   **/
   DISPLAY(1);
   private final int value;
   private TUserShowFieldIsshow(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
