package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.domain.TUserImportRecord;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecordQueryReq;
import com.talebase.cloud.base.ms.examer.enums.TUserImportRecordStatus;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
public class UserImportRecordSqlProvider {

    public String insert(TUserImportRecord importRecord){
        return new SQL(){{
            INSERT_INTO("t_user_import_record");
            VALUES("batch_no", "#{batchNo}");
            VALUES("status", "#{status}");
            VALUES("examinee", "#{examinee}");
            VALUES("remark", "#{remark}");
            if(!StringUtil.isEmpty(importRecord.getDetailJson()))
                VALUES("detail_json", "#{detailJson}");
        }}.toString();
    }

    /**
     * 统计批次下的成功、失败导入条数
     * @param batchNo
     * @return
     */
    public String queryStaticsNum(String batchNo){
        return "select sum(suc_num) suc_num, sum(fail_num) fail_num from (\n" +
                "select \n" +
                    "case when status = 1 then 1 else 0 end as suc_num,\n" +
                    "case when status = 0 then 1 else 0 end as fail_num\n" +
                    "from t_user_import_record \n" +
                    "where batch_no = #{batchNo}\n" +
                ") s";
    }


    public String query(DUserImportRecordQueryReq queryReq, ServiceHeader serviceHeader, PageRequest pageReq){
        String sql = new SQL(){{
            SELECT("r.*");
            SELECT("l.creater, l.created_date");
            FROM("t_user_import_record r");
            JOIN("t_user_import_log l on r.batch_no = l.batch_no ");
            WHERE("1 = 1");
        }}.toString();

        sql = sql + initConditionSql(queryReq);
        sql = sql + " order by r.batch_no desc, r.id desc ";
        sql = sql + SqlBuilderUtil.buildPageLimit(pageReq);
        return sql;
    }

    public String queryCnt(DUserImportRecordQueryReq queryReq, ServiceHeader serviceHeader){
        String sql = new SQL(){{
            SELECT("count(*)");
            FROM("t_user_import_record r");
            JOIN("t_user_import_log l on r.batch_no = l.batch_no ");
            WHERE("1 = 1");
        }}.toString();

        sql = sql + initConditionSql(queryReq);
        return sql;
    }

    private String initConditionSql(DUserImportRecordQueryReq queryReq){
        StringBuffer sql = new StringBuffer("");
        if(queryReq != null){
            if(!StringUtil.isEmpty(queryReq.getAccount()))
                sql.append(" and l.creater = #{queryReq.account} ");
            if(!StringUtil.isEmpty(queryReq.getBatchNo()))
                sql.append(" and r.batch_no = #{queryReq.batchNo} ");
            if(queryReq.getStatus() != null)
                sql.append(" and r.batch_no = #{queryReq.status} ");
            if(queryReq.getProjectId() != null && queryReq.getProjectId() > 0)
                sql.append(" and l.project_id = #{queryReq.projectId} ");
            if(queryReq.getTaskId() != null && queryReq.getTaskId() > 0)
                sql.append(" and l.task_id = #{queryReq.taskId} ");
        }
        return sql.toString();
    }

    public String findFailRecords(String batchNo){
        return new SQL(){{
            SELECT("*");
            FROM("t_user_import_record ");
            WHERE("batch_no = #{batchNo}");
            WHERE("status = " + TUserImportRecordStatus.FAIL.getValue());
        }}.toString();
    }


}
