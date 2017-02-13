package com.talebase.cloud.ms.project.dao;

import com.talebase.cloud.base.ms.project.domain.TProject;
import com.talebase.cloud.base.ms.project.dto.DProjectQueryReq;
import com.talebase.cloud.base.ms.project.enums.TProjectScanEnable;
import com.talebase.cloud.base.ms.project.enums.TProjectStatus;
import com.talebase.cloud.base.ms.project.enums.TTaskStatus;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.util.PermissionUtil;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class ProjectSqlProvider {

    public String updateStatus(){
        return new SQL(){{
            UPDATE("t_project");
            SET("status = #{newStatus}");
            SET("modifier = #{operatorName}");
            SET("modified_date = now()");
            WHERE("id = #{projectId}");
        }}.toString();
    }

    public String get(){
        return "select t.*, ga.org_code from t_project t join t_group_admin ga on t.creater = ga.account and t.company_id = ga.company_id where t.id = #{projectId}";
    }

    public String getByTask(Integer taskId){
        return "select p.*, ga.org_code from t_project p " +
                "join t_group_admin ga on p.creater = ga.account and p.company_id = ga.company_id " +
                "where p.id in ( " +
                    "select project_id from t_task t where id = #{taskId} " +
                ")";
    }

//    public String find(){
//        return new SQL(){{
//            SELECT("*");
//            FROM("t_project");
//            WHERE("status in (" + TProjectStatus.ENABLE.getValue() + ", " + TProjectStatus.DISABLE.getValue() + ")");
//            WHERE("company_id = #{companyId}");
//            WHERE("g.orgCode like concat('%', #{orgCode}, '%')");
//        }}.toString();
//    }

    public String insert(TProject project){
        return new SQL(){{
            INSERT_INTO("t_project");

            VALUES("name", "#{name}");
            VALUES("description", "#{description}");
            VALUES("company_id", "#{companyId}");
            VALUES("creater", "#{creater}");
            VALUES("created_date", "now()");
            VALUES("modifier", "#{modifier}");
            VALUES("modified_date", "now()");
            VALUES("status", "#{status}");
            VALUES("start_date", "#{startDate}");
            VALUES("end_date", "#{endDate}");

            if(TProjectScanEnable.ENABLE.getValue() == project.getScanEnable()){//开通扫码
                VALUES("scan_enable", TProjectScanEnable.ENABLE.getValue() + "");
                VALUES("scan_max", "#{scanMax}");
                VALUES("scan_now", "0");
                VALUES("scan_account_pre", "#{scanAccountPre}");
                VALUES("scan_start_date", "#{scanStartDate}");
                VALUES("scan_end_date", "#{scanEndDate}");
            }else{//不开通扫码
            }
        }}.toString();
    }

    public String insertProjectAdmin(){
        return new SQL(){{
            INSERT_INTO("t_project_admin");
            VALUES("project_id", "#{projectId}");
            VALUES("account", "#{account}");
            VALUES("name", "#{name}");
        }}.toString();
    }

    public String update(TProject project){
        return new SQL(){{
            UPDATE("t_project");
            SET("name = #{name}");
            SET("description = #{description}");
            SET("modifier = #{modifier}");
            SET("modified_date = now()");
            SET("start_date = #{startDate}");
            SET("end_date = #{endDate}");
            if(TProjectScanEnable.ENABLE.getValue() == project.getScanEnable()){//开通扫码
                SET("scan_enable = " + TProjectScanEnable.ENABLE.getValue() + "");
                SET("scan_max = #{scanMax}");
                SET("scan_account_pre = #{scanAccountPre}");
                SET("scan_start_date = #{scanStartDate}");
                SET("scan_end_date = #{scanEndDate}");
                SET("scan_image = #{scanImage}");
            }else{//不开通扫码
                SET("scan_enable = " + TProjectScanEnable.DISABLE.getValue() + "");
                SET("scan_now = 0");
            }
            WHERE("id = #{id}");
        }}.toString();
    }

    public String query(String account, Integer companyId, String orgCode, DProjectQueryReq queryReq, PageRequest pageReq){

        String sql = new SQL(){{
            SELECT("p.*");
            SELECT("pen.err_num");
            FROM("t_project p");
            JOIN("t_project_err_notify pen on p.id = pen.project_id");
        }}.toString();

//        sql = sql + PermissionUtil.getDataPermissionSql("p.creater") + " where 1 = 1 " + initConditionSql(companyId, orgCode, queryReq);
        sql = sql + " where 1 = 1 " + initConditionSql(queryReq, companyId) + PermissionUtil.projectExPermissionSqlIn("p.id", "p.creater");
        sql = sql + " order by p.id desc ";
        sql = sql + SqlBuilderUtil.buildPageLimit(pageReq);

        return sql;
    }

    public String queryTotal(String account, Integer companyId, String orgCode, DProjectQueryReq queryReq){
        String sql = new SQL(){{
            SELECT("count(*)");
            FROM("t_project p");
        }}.toString();
//        sql = sql + PermissionUtil.getDataPermissionSql("p.creater") + " where 1 = 1 " + initConditionSql(companyId, orgCode, queryReq);
        sql = sql + " where 1 = 1 " + initConditionSql(queryReq, companyId) + PermissionUtil.projectExPermissionSqlIn("p.id", "p.creater");
        return sql;
    }

    public String findSelect(String account, Integer companyId, String orgCode){
        String sql = new SQL(){{
            SELECT("id, name");
            FROM("t_project p");
        }}.toString();
//        sql = sql + PermissionUtil.getDataPermissionSql("p.creater") + " where 1 = 1 " + initConditionSql(companyId, orgCode, queryReq);
        sql = sql + " where 1 = 1 " + initConditionSql(null, companyId) + PermissionUtil.projectExPermissionSqlIn("p.id", "p.creater");
        return sql;
    }

    private String initConditionSql(DProjectQueryReq queryReq, Integer companyId){

        StringBuffer conditionSql = new StringBuffer();
        conditionSql.append("and p.company_id = #{companyId} ");
        conditionSql.append("and p.status in (" + TProjectStatus.ENABLE.getValue() + ", " + TProjectStatus.DISABLE.getValue() + ") ");
//        conditionSql.append("and p.company_id = #{companyId} ");
//        conditionSql.append("and p.org_code like concat('%', #{orgCode}, '%') ");

        if(queryReq != null){
            if(!StringUtil.isEmpty(queryReq.getProjectNameLike())){
                conditionSql.append("and p.name like concat('%', #{queryReq.projectNameLike}, '%') ");
            }
            if(!StringUtil.isEmpty(queryReq.getTaskNameLike())){
                conditionSql.append("and p.id in ( ");
                conditionSql.append(" select t.project_id from t_task t " +
                        "where t.name like concat('%', #{queryReq.taskNameLike}, '%') " +
                        "and t.status in (" + TTaskStatus.ENABLE.getValue() + ", " + TTaskStatus.DISABLE.getValue() + ") " +
                        ")");
            }
        }

        return conditionSql.toString();
    }

    public String getProjectCntByName(Integer companyId, String name, Integer projectId){
        return new SQL(){{
            SELECT("count(*)");
            FROM("t_project ");
            WHERE("name = #{name}");
            WHERE("company_id = #{companyId}");
            WHERE("status in (" + TProjectStatus.ENABLE.getValue() + ", " + TProjectStatus.DISABLE.getValue() + ") ");
            if(projectId != null && projectId > 0){
                WHERE("id <> #{projectId} ");
            }
        }}.toString();
    }

    public String queryTasksByUserId(Integer userId){
        String sql = "SELECT p.id project_id,p.name project_name,p.description,p.status project_status," +
                " p.start_date project_start_date,p.end_date project_end_date,t.id task_id, t.name task_name, t.start_date task_start_date, t.end_date task_end_date, t.status task_status, t.latest_start_date as task_latest_end_date, " +
                " paper_num,exam_time,paper_id," +
                " CASE WHEN v.finish_num = 1 THEN '已完成'" +
                " WHEN v.finish_num = 0 and v.in_num = 1 THEN '答题中'" +
                " WHEN v.finish_num = 0 and v.in_num = 0 and v.pre_num = 1 THEN '未开始' ELSE '其他' END examineeStatus" +
                " FROM t_user_exam ue" +
                " JOIN v_task_progress v ON v.user_id = ue.user_id " +
                " AND v.project_id = ue.project_id " +
                " AND v.task_id = ue.task_id" +
                " JOIN t_project p ON ue.project_id = p.id" +
                " JOIN t_task t on ue.task_id = t.id" +
                " WHERE ue.user_id = #{userId}" +
                " and p.status not in (" + TProjectStatus.DELETE.getValue() + ")" +
                " and t.status not in (" + TTaskStatus.DELETE.getValue() + ")" +
//                " and p.status in ( " + TProjectStatus.ENABLE.getValue() + ") " +
//                " and (p.start_date is null or p.start_date < now()) " +
//                " and (p.end_date is null or p.end_date > now()) " +
                " ORDER BY p.created_date desc,t.created_date ASC";
        return sql;
    }
}
