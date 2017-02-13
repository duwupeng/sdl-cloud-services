package com.talebase.cloud.base.ms.project.enums;
/**
*是否开启扫码
**/
public enum TProjectScanEnable{
   /**
   *否
   **/
   DISABLE(0),

   /**
   *开启
   **/
   ENABLE(1);
   private final int value;
   private TProjectScanEnable(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
