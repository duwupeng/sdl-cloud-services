package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.domain.TUserImportLog;
import com.talebase.cloud.base.ms.examer.enums.TUserImportLogStatus;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
public class UserImportLogSqlProvider {

    public String insert(TUserImportLog importLog){
        return new SQL(){{
            INSERT_INTO("t_user_import_log");
            VALUES("batch_no", "#{batchNo}");
            VALUES("status", "" + TUserImportLogStatus._ON_DOING.getValue());
            VALUES("creater", "#{creater}");
            VALUES("created_date", "now()");
            VALUES("company_id", "#{companyId}");
            VALUES("title_json", "#{titleJson}");
            if(importLog.getProjectId() != null)
                VALUES("project_id", "#{projectId}");
            if(importLog.getTaskId() != null)
                VALUES("task_id", "#{taskId}");
        }}.toString();
    }

    public String updateToEnd(TUserImportLog importLog){
        return new SQL(){{
            UPDATE("t_user_import_log");
            SET("status = #{status}" );
            if(importLog.getSuccNum() != null && importLog.getFailNum() != null){
                SET("succ_num = #{succNum}");
                SET("fail_num = #{failNum}");
            }
            SET("finished_date = now()");
            if(!StringUtil.isEmpty(importLog.getTitleJson()))
                SET("title_json = #{titleJson}");
            WHERE("id = #{id}");
        }}.toString();
    }
}
