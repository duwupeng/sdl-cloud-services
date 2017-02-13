package com.talebase.cloud.ms.consumption.dao;

import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.consume.dto.DAccountCondition;
import com.talebase.cloud.common.protocal.PageRequest;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by suntree.xu on 2016-12-7.
 */
public class AccountLineProvider {

    /**
     * 查询消费记录sql
     *
     * @param reqCondition
     * @param pageRequest
     * @return
     */
    public String buildGetAccountConsumes(DAccountCondition reqCondition, PageRequest pageRequest) {
        String sql = "select modifier as account,sum(point_var) as pointUsed," +
                "sum(sms_var) as smsUsed,project_id as projectId,task_id as taskId " +
                "from t_account_line where type=1";
        if(reqCondition.getAccount() != null){
            sql += " and modifier like '%" + reqCondition.getAccount() + "%'";
        }
        if(reqCondition.getProjectId()>0){
            sql += " and project_id=" + reqCondition.getProjectId();
        }
        if(reqCondition.getTaskId()>0){
            sql += " and task_id=" + reqCondition.getTaskId();
        }
        /*if (reqCondition.getAccount() != null && (reqCondition.getProjectId() == 0 && reqCondition.getTaskId() == 0)) {
            sql += "modifier='" + reqCondition.getAccount() + "'";
        } else if (reqCondition.getAccount() != null && (reqCondition.getProjectId() > 0 && reqCondition.getTaskId() == 0)) {
            sql += "modifier='" + reqCondition.getAccount() + "' and project_id=" + reqCondition.getProjectId();
        } else if (reqCondition.getAccount() != null && (reqCondition.getProjectId() > 0 && reqCondition.getTaskId() > 0)) {
            sql += "modifier='" + reqCondition.getAccount() + "' and project_id=" + reqCondition.getProjectId() + " and task_id=" + reqCondition.getTaskId();
        }*/
        sql += " group by modifier,project_id,task_id ";
        sql = sql + "limit " + pageRequest.getStart() + "," + pageRequest.getLimit();
        return sql;
    }

    /**
     * 查询消费记录总数sql
     *
     * @param reqCondition
     * @return
     */
    public String buildGetAccountConsumesTotal(DAccountCondition reqCondition) {
        String sql = "select count(*) cnt from (select modifier,project_id,task_id "
                + "from t_account_line where type=1";
        if(reqCondition.getAccount() != null){
            sql += " and modifier like '%" + reqCondition.getAccount() + "%'";
        }
        if(reqCondition.getProjectId()>0){
            sql += " and project_id=" + reqCondition.getProjectId();
        }
        if(reqCondition.getTaskId()>0){
            sql += " and task_id=" + reqCondition.getTaskId();
        }
       /* if (reqCondition.getAccount() != null && (reqCondition.getProjectId() == 0 && reqCondition.getTaskId() == 0)) {
            sql += "modifier='" + reqCondition.getAccount() + "'";
        } else if (reqCondition.getAccount() != null && (reqCondition.getProjectId() > 0 && reqCondition.getTaskId() == 0)) {
            sql += "modifier='" + reqCondition.getAccount() + "' and project_id=" + reqCondition.getProjectId();
        } else if (reqCondition.getAccount() != null && (reqCondition.getProjectId() > 0 && reqCondition.getTaskId() > 0)) {
            sql += "modifier='" + reqCondition.getAccount() + "' and project_id=" + reqCondition.getProjectId() + " and task_id=" + reqCondition.getTaskId();
        }*/
        sql += " group by modifier,project_id,task_id)a";
        return sql;
    }

    /**
     * 查询消费全部记录sql
     *
     * @param reqCondition
     * @return
     */
    public String buildGetAccountConsumesTotalList(DAccountCondition reqCondition, int companyId) {
        String sql = "select modifier as account,sum(point_var) as pointUsed," +
                "sum(sms_var) as smsUsed,project_id as projectId,task_id as taskId " +
                "from t_account_line where type=1 and company_id=" + companyId;
        sql += " group by modifier,project_id,task_id";
        return sql;
    }

    /**
     * 查询充值记录sql
     *
     * @param account
     * @param pageReq
     * @return
     */
    public String buildGetAccountPays(String account, PageRequest pageReq) {
        String sql = "select modifier as account,point_var as pointVar,sms_var as smsVar," +
                "modified_date as modifiedDate " +
                "from t_account_line where type=2 and modifier like '%" + account + "%' ";
        sql = sql + "limit " + pageReq.getStart() + "," + pageReq.getLimit();
        return sql;
    }

    /**
     * 查询充值记录总数sql
     *
     * @param account
     * @return
     */
    public String buildGetAccountPayTotal(String account) {
        String sql = "select count(*) "
                + "from t_account_line where type=2 and modifier like '%" + account + "%' ";
        return sql;
    }

    /**
     * 查询全部消费记录
     *
     * @param companyId
     * @return
     */
    public String buildGetAccountPayTotalList(int companyId) {
        String sql = "select modifier as account,point_var as pointVar,sms_var as smsVar," +
                "modified_date as modifiedDate " +
                "from t_account_line where type=2 and company_id=" + companyId;
        return sql;
    }

    /**
     * 查询账户余额sql
     *
     * @param company_id
     * @return
     */
    public String buildGetAccount(Integer company_id) {
        String sql = "select * from t_account where company_id=" + company_id;
        return sql;
    }

    /**
     * 增加操作记录sql
     *
     * @param tAccountLine
     * @return
     */
    public String buildInsertAccountLine(TAccountLine tAccountLine) {
        return new SQL() {{
            INSERT_INTO("t_account_line");
            VALUES("company_id", "#{companyId}");
            VALUES("modifier", "#{modifier}");
            VALUES("modified_date", "now()");
            VALUES("remark", "#{remark}");
            VALUES("type", "#{type}");
            VALUES("point_var", "#{pointVar}");
            VALUES("sms_var", "#{smsVar}");
            VALUES("project_id", "#{projectId}");
            VALUES("task_id", "#{taskId}");
        }}.toString();
    }

    public String buildUpdateAccount(TAccount tAccount) {
        return new SQL() {{
            UPDATE("t_account");
            SET("point_balance = #{pointBalance}");
            SET("sms_balance = #{smsBalance}");
            SET("modified_date = now()");
            SET("modifier = #{modifier}");
            WHERE("company_id = #{companyId}");
        }}.toString();
    }

}
