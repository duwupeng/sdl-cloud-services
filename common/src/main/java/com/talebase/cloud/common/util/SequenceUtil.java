package com.talebase.cloud.common.util;

import java.text.*;
import java.util.Calendar;

/**
 * Created by eric.du on 2016-12-6.
 */
public class SequenceUtil {

    /** The FieldPosition. */
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);

    /** This Format for format the data to special format. */
    private final static Format dateFormat = new SimpleDateFormat("MMddHHmmssS");
    /**
     * 时间格式生成序列
     * @return String
     */
    public static synchronized String generateSequenceNo(String prefex) {

        Calendar rightNow = Calendar.getInstance();

        StringBuffer sb = new StringBuffer();

        dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);
        return prefex + sb.toString();
    }
}
