package com.talebase.cloud.common.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Eric on 2016/8/17.
 */
public class TimeUtil {
    private static SimpleDateFormat second = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
    private static SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat detailDay = new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static SimpleDateFormat fileMinute = new SimpleDateFormat("yyyyMMddHHmm");
    private static SimpleDateFormat tempTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat excelDate = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 格式化excel中的时间
     * @param date
     * @return
     */
    public static String formatDateForExcelDate(Date date) {
        return excelDate.format(date);
    }

    /**
     * 将日期格式化作为文件名
     * @param date
     * @return
     */
    public static String formatDateForFileName(Date date) {
        return fileName.format(date);
    }

    /**
     * 将日期格式化作为文件名(到分钟)
     * @param date
     * @return
     */
    public static String formatDateForFileMinute(Date date) {
        return fileMinute.format(date);
    }

    /**
     * 格式化日期(精确到秒)
     *
     * @param date
     * @return
     */
    public static String formatDateSecond(Date date) {
        return second.format(date);
    }

    /**
     * 格式化日期(精确到秒)
     *
     * @param date
     * @return
     */
    public static String tempDateSecond(Date date) {
        return tempTime.format(date);
    }

    public static Date tempDateSecond(String str) {
        try {
            return tempTime.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
    /**
     * 格式化日期(精确到天)
     *
     * @param date
     * @return
     */
    public static String formatDateDay(Date date) {
        return day.format(date);
    }

    /**
     * 格式化日期(精确到天)
     *
     * @param date
     * @return
     */
    public static String formatDateDetailDay(Date date) {
        return detailDay.format(date);
    }

    /**
     * 将double类型的数字保留两位小数（四舍五入）
     *
     * @param number
     * @return
     */
    public static String formatNumber(double number) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("#0.00");
        return df.format(number);
    }

    /**
     * 将字符串转换成日期
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static Date formateDate(String date) throws Exception {
        return day.parse(date);
    }

    /**
     * 将字符日期转换成Date
     * @param date
     * @return
     * @throws Exception
     */
    public static Date parseStringToDate(String date) throws Exception {
        return day.parse(date);
    }

    public static String formatDoubleNumber(double number) {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(number);
    }

    /**
     * UNIX时间戳
     * @return
     */
    public static long getTimestamp(){
        return  System.currentTimeMillis()/1000L;
    }

    /**
     *
     * @param lo
     * @return
     */
    public static String longToDate(long lo){
        if (StringUtil.isEmpty(lo)){
            return "";
        }
        Date date = new Date(lo);
        return day.format(date);
    }
}

