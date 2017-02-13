package com.talebase.cloud.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
public class NumberUtil {


    public static Boolean isEmpty(Integer num) {
        return num == null || num <= 0;
    }

    //double 类型保留一位小数
    public static double changeDouble(Double dou) {
        NumberFormat nf = new DecimalFormat("000.0 ");
        dou = Double.parseDouble(nf.format(dou));
        return dou;
    }
    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
//        Pattern pattern = Pattern.compile("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+");//BigDecimal
        Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static double add(double v1, double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
}
