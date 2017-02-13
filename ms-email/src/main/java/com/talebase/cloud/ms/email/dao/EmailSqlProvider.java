package com.talebase.cloud.ms.email.dao;


import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.base.ms.notify.domain.TNotifyRecord;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;


/**
 * Created by kanghong.zhao on 2016-11-7.
 */
public class EmailSqlProvider {

    public String buildInsertSql(DEmailLog tNotifyRecord){
        return new SQL(){{
            INSERT_INTO("t_email_log");
            VALUES("table_name", "#{tableName}");
            VALUES("table_id", "#{tableId}");
            VALUES("send_content",  "#{sendContent}");
            VALUES("send_time",  "now()");
            VALUES("sender",  "#{sender}");
            VALUES("sendTimes",  "#{sendTimes}");
            VALUES("status",  "#{status}");
            VALUES("subject",  "#{subject}");
            VALUES("email",  "#{email}");
        }}.toString();
    }

    public String buildSelectSql(List<String> tableNames,List<String> tableIds,List<String> status){
        return new SQL(){{
            SELECT(" * ");
            FROM(" t_email_log tl ");
            WHERE(" 1=1 ");
            if (tableNames.size()>0){
                AND();
                WHERE("tl.table_name " + SqlBuilderUtil.buildInSql("tableNames", tableNames.size()));
            }
            if (tableIds.size()>0){
                AND();
                WHERE("tl.table_id " + SqlBuilderUtil.buildInSql("tableIds", tableIds.size()));
            }
            if (status.size()>0){
                AND();
                WHERE("tl.status " + SqlBuilderUtil.buildInSql("statuss", status.size()));
            }
        }}.toString();
    }
}
