package com.talebase.cloud.common.util;

import com.talebase.cloud.common.protocal.PageRequest;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
public class SqlBuilderUtil {

    private static String buildDynamicCollectionSql(String paramName, int size, boolean include){
        String includeStr = include ? " in " : " not in ";

        StringBuilder inSql = new StringBuilder(includeStr + "(#{" + paramName + "[0]}");
        for(int i = 1; i < size; i++){
            inSql.append(", #{" + paramName + "[" + i + "]}");
        }

        inSql.append(")");
        return inSql.toString();
    }

    public static String buildNotInSql(String paramName, int size){
        return buildDynamicCollectionSql(paramName, size, false);
    }

    public static String buildInSql(String paramName, int size){
        return buildDynamicCollectionSql(paramName, size, true);
    }

    /**
     * 调用此方法组拼sql语句，分页参数必须以pageReq命名
     * @param pageReq
     * @return
     */
    public static String buildPageLimit(PageRequest pageReq){
        StringBuilder sql = new StringBuilder();

        if(pageReq.getStart() != null && pageReq.getLimit() != null){
            sql.append(" limit #{pageReq.start}, #{pageReq.limit}");
        }
        return sql.toString();
    }

    public static String toDateFormat(String param){
        return "str_to_date(#{" + param + "},'%Y-%m-%d %H:%i:%s')";
    }

}
