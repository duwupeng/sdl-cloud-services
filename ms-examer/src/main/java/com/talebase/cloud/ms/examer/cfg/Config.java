package com.talebase.cloud.ms.examer.cfg;

/**
 * Created by daorong.li on 2016-12-7.
 */
public class Config {

   public final static String FIELD_PREFIX = "ExtensionField";
   public final static String FIELDKEY = "fieldKey";
   public final static Integer SQL_IN_LIMIT_NUMBER = 100;
   public final static String PSSOWR_FIELD ="password";//加密字段，用于显示密码时匹配解密字段
   public final static String ACCOUNT_FIELD ="account";//账号字段，账号管理时默认显示、必填、唯一;账户设置时必填默认显示
   public final static String MOBILE_FIELD ="mobile";//手机字段，账户设置时必填默认显示
   public final static String EMAIL_FIELD ="email";//邮件字段，账户设置时必填默认显示
   public final static Integer FIEL_LENGTH = 50;//扩展字段长度为50
   public final static Integer FIELD_NUMBER = 20;//每个公司最多扩展字段为20个

}
