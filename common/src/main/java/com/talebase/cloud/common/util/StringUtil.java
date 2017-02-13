package com.talebase.cloud.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-11-29.
 */
public class StringUtil {

    public static final String COMMA = ",|\\，";

    public static List<Integer> toIntList(String idsStr, String spliter) {
        List<Integer> ids = new ArrayList<>();

        if (isEmpty(idsStr)) {
            return ids;
        }
        for (String idStr : idsStr.split(spliter)) {
            ids.add(Integer.parseInt(idStr));
        }
        return ids;
    }

    public static List<Integer> toIntListByComma(String idsStr) {
        return toIntList(idsStr, COMMA);
    }

    public static List<String> toStrListBySpliter(String strsStr, String spliter) {
        List<String> strs = new ArrayList<>();

        if (isEmpty(strsStr)) {
            return strs;
        }
        for (String str : strsStr.split(spliter)) {
            strs.add(str);
        }
        return strs;
    }

    public static List<String> toStrListByComma(String strsStr) {
        return toStrListBySpliter(strsStr, COMMA);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.trim().isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || "".equals(obj);
    }

    public static String toStrByComma(List<String> objs) {
        StringBuffer sb = new StringBuffer();
        for (Object obj : objs) {
            sb.append(obj).append(COMMA);
        }

        sb = sb.deleteCharAt(sb.lastIndexOf(COMMA));
        return sb.toString();
    }

    /**
     * 排除HTML标签
     *
     * @param content
     * @return
     */
    public static String replaceHtml(String content) {
        if (isEmpty(content)) return "";
        return content.replaceAll("</?[^>]+>", "").replaceAll("&nbsp;", " ");
    }

}
