package com.talebase.cloud.base.ms.examer.enums;
/**
*测试类型
**/
public enum TUserExamType{
   /**
   *考试
   **/
   EXAM(1),

   /**
   *360测试
   **/
   TEST360(2),

   /**
   *在线心理测评
   **/
   PSYCHOLOGICAL_TEST(3),

   /**
   *调用
   **/
   RESEARCH(4);
   private final int value;
   private TUserExamType(int value) {
     this.value = value;
   }
   public int getValue() {
      return value;
   }
}
