package com.talebase.cloud.common.util;

import java.sql.Timestamp;

/**
 * Created by kanghong.zhao on 2016-12-2.
 */
public class SqlUtil {

    public static Timestamp tempDateSecond(String str) {
        return tempDateSecondAddTime(str, 0, 0, 0);
//        if(str == null)
//            return null;
//        return new Timestamp(TimeUtil.tempDateSecond(str).getTime());
    }

    public static Timestamp tempDateSecondAddTime(String str, int hour, int minute, int second){
        if(StringUtil.isEmpty(str))
            return null;
        return new Timestamp(TimeUtil.tempDateSecond(str).getTime() + toMs(hour, minute, hour));
    }

    public static Timestamp tempDateSecondAddMinute(String str, int minute) {
        return tempDateSecondAddTime(str, 0, minute, 0);
    }

    private static long toMs(int hour, int minute, int second){
        return 60 * 60 * 1000 * hour + 60 * 1000 * minute + 1000 * second;
    }

    public static String coverBaifenhao(String str){
        if(StringUtil.isEmpty(str))
            return str;

        if(!str.contains("%"))
            return str;

        return str.replace("%", "\\%");
    }

}
