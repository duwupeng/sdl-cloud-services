package com.talebase.cloud.common.util;

import org.apache.commons.beanutils.BeanUtils;
/**
 * Created by eric.du on 2016-12-1.
 * java.lang.BigDecimal
 *java.lang.BigInteger
 *boolean and java.lang.Boolean
 *byte and java.lang.Byte
 *char and java.lang.Character
 *java.lang.Class
 *double and java.lang.Double
 *float and java.lang.Float
 *int and java.lang.Integer
 *long and java.lang.Long
 *short and java.lang.Short
 *java.lang.String
 *java.sql.Date
 *java.sql.Time
 *java.sql.Timestamp
 */
public class BeanConverter {
    public static void copyProperties(java.lang.Object dest,java.lang.Object orig)
            throws java.lang.IllegalAccessException,
            java.lang.reflect.InvocationTargetException{
        BeanUtils.copyProperties(dest,orig);
    }
}



