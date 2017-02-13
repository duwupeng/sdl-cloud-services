package com.talebase.cloud.ms.project.dao;

import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.base.ms.project.dto.DUseStaticsQueryReq;
import com.talebase.cloud.base.ms.project.enums.TTaskStatus;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class TaskSqlProvider {

    public String updateStatusByProject(){
        return new SQL(){{
            UPDATE("t_task");
            SET("status = #{newStatus}");
            SET("modifier = #{operatorName}");
            SET("modified_date = now()");
            WHERE("project_id = #{projectId}");
            WHERE("status in (" + TTaskStatus.ENABLE.getValue() + "," + TTaskStatus.DISABLE.getValue() + ")");
//            if(TTaskStatus.DELETE.getValue() == newStatus){

//            }
        }}.toString();
    }

    public String updateStatus(){
        return new SQL(){{
            UPDATE("t_task");
            SET("status = #{newStatus}");
            SET("modifier = #{operatorName}");
            SET("modified_date = now()");
            WHERE("id = #{taskId} ");
        }}.toString();
    }

    public String get(){
        return "select * from t_task where id = #{taskId}";
    }

    public String findTasksByProjectIds(List<Integer> projectIds){
       String sql = "select * from t_task t \n"
               + "join t_task_progress tp on t.id = tp.task_id \n" +
               "where t.status in (" + TTaskStatus.ENABLE.getValue() + "," + TTaskStatus.DISABLE.getValue() + ") \n"
               + "and t.project_id " + SqlBuilderUtil.buildInSql("projectIds", projectIds.size()) + " \n"
               + "order by t.project_id ";
        return sql;
    }

    public String findTaskExById(Integer id){
       String sql = "select * from t_task t \n"
               + "join t_task_progress tp on t.id = tp.task_id \n" +
               "where t.status in (" + TTaskStatus.ENABLE.getValue() + "," + TTaskStatus.DISABLE.getValue() + ") \n"
               + "and t.id =" + id;
        return sql;
    }

    public String insert(TTask task){
        return new SQL(){{
            INSERT_INTO("t_task");
            VALUES("project_id", "#{projectId}");
            VALUES("name", "#{name}");
            VALUES("company_id", "#{companyId}");
            VALUES("creater", "#{creater}");
            VALUES("created_date", "now()");
            VALUES("modifier", "#{modifier}");
            VALUES("modified_date", "now()");
            VALUES("status", "#{status}");
//            VALUES("status", TTaskStatus.ENABLE.getValue() + "");

            if(task.getPageChangeLimit() != null && task.getPageChangeLimit() > 0){
                VALUES("page_change_limit", "#{pageChangeLimit}");
            }

            VALUES("paper_id", "#{paperId}");
//            if(task.getNeedMarkingNum() == null){
                VALUES("need_marking_num", "#{needMarkingNum}");
//            }
            VALUES("paper_unicode", "#{paperUnicode}");
            VALUES("paper_version", "#{paperVersion}");
            VALUES("paper_num", "#{paperNum}");

            VALUES("exam_time", "#{examTime}");
            VALUES("latest_start_date", "#{latestStartDate}");
            VALUES("start_date", "#{startDate}");
            VALUES("end_date", "#{endDate}");
        }}.toString();
    }

    public String update(TTask task){
        return new SQL(){{
            UPDATE("t_task");
            SET("name = #{name}");
//            SET("company_id = #{companyId}");
            SET("modifier = #{modifier}");
            SET("modified_date = now()");

            if(task.getPageChangeLimit() != null && task.getPageChangeLimit() > 0){
                SET("page_change_limit = #{pageChangeLimit}");
            }else{
                SET("page_change_limit = null");
            }

            SET("exam_time = #{examTime}");
            SET("latest_start_date = #{latestStartDate}");
            SET("start_date = #{startDate}");
            SET("end_date = #{endDate}");
            WHERE("id = #{id}");
        }}.toString();
    }

    public String updatePaper(TTask task){
        return new SQL(){{
            UPDATE("t_task");
            SET("modifier = #{modifier}");
            SET("modified_date = now()");
            SET("paper_id = #{paperId}");
            SET("need_marking_num = #{needMarkingNum}");
            SET("paper_unicode = #{paperUnicode}");
            SET("paper_version = #{paperVersion}");
            SET("paper_num = #{paperNum}");
            SET("name = #{name}");
            WHERE("id = #{id}");
        }}.toString();
    }

    public String insertTaskExaminer(){
        return new SQL(){{
            INSERT_INTO("t_task_examiner");
            VALUES("task_id", "#{taskId}");
            VALUES("examiner", "#{examiner}");
            VALUES("name", "#{name}");
        }}.toString();
    }

    public String getTaskExaminers(List<Integer> taskIds){
        String sql = new SQL(){{
            SELECT("*");
            FROM("t_task_examiner");
            WHERE("task_id " + SqlBuilderUtil.buildInSql("taskIds", taskIds.size()) );
        }}.toString();
        return sql;
    }

    public String findTasksNumByExaminer(String operatorName, Integer companyId){
        String sql =
                "select count(t.id) from t_task t\n" +
                        "where t.status not in (" + TTaskStatus.DELETE.getValue() + ") \n" +
                        "and t.company_id = #{companyId}\n" +
                        "and t.id in (\n" +
                        "select task_id from t_task_examiner \n" +
                        "where examiner = #{operatorName}\n" +
                        ") \n";
        return sql;
    }

    public String findTasksByExaminer(String operatorName, Integer companyId, PageRequest pageRequest){
        String sql =
                "select t.name as task_name, t.id as task_id, t.need_marking_num,\n" +
                        "p.name as project_name, p.id as project_id, p.start_date as project_start_date, p.end_date as project_end_date, " +
                        "case when vs.finish_num is null then 0 else vs.finish_num end as finish_num, \n" +
                        "case when vs.marked_num is null then 0 else vs.marked_num end as marked_num, \n" +
                        "case when te.account_names is not null then account_names else '' end as account_names \n" +
                        "from t_task t \n" +
                        "left join (\n" +
                        "select task_id, GROUP_CONCAT(name separator ',') account_names from t_task_examiner \n" +
                        "group by task_id\n" +
                        ") te on t.id = te.task_id \n" +
                        "join t_project p on t.project_id = p.id\n" +
                        "left join t_task_progress vs on t.id = vs.task_id\n" +
                        "where t.status not in (" + TTaskStatus.DELETE.getValue() + ") \n" +
                        "and t.company_id = #{companyId}\n" +
                        "and t.need_marking_num is not null\n" +
                        "and t.need_marking_num > 0\n" +
                        "and t.id in (\n" +
                        "select task_id from t_task_examiner \n" +
                        "where examiner = #{operatorName}\n" +
                        ") \n";
        sql += "order by t.id desc \n";
        sql += SqlBuilderUtil.buildPageLimit(pageRequest);
        return sql;
    }

    public String getTaskInMark(String operatorName, Integer companyId){
        String sql =
                "select count(distinct task_id) as total_task_num, count(distinct done_task_id) as done_task_num,\n" +
                        "case when sum(total_paper_num) is null then 0 else sum(total_paper_num) end as total_paper_num, \n" +
                        "case when sum(done_paper_num) is null then 0 else sum(done_paper_num) end as done_paper_num\n" +
                        "from (\n" +
                        "select task_id, case when finish_num = marked_num and finish_num>0 then task_id else null end as done_task_id, \n" +
                        "case when finish_num is null then 0 else finish_num end as total_paper_num, \n" +
                        "case when marked_num is null then 0 else marked_num end as done_paper_num \n" +
                        "from t_task_progress v\n" +
                        "where task_id in (\n" +
                        "select id from t_task \n" +
                        "where id in (\n" +
                        "select task_id from t_task_examiner where examiner = #{operatorName}\n" +
                        ")\n" +
                        "and status not in (" + TTaskStatus.DELETE.getValue() + ")\n" +
                        "and company_id = #{companyId}\n" +
                        ")\n" +
                        ") a";
        return sql;
    }

    public String findUseStatics(List<DUseStaticsQueryReq> reqs, ServiceHeader serviceHeader){
        StringBuffer sb = new StringBuffer();
        sb.append("select a.*, t.name as task_name, p.name as project_name from \n" +
                "(select project_id, task_id, creater as account, \n" +
                "sum(pre_num) as pre_num, sum(in_num) as in_num, \n" +
                "sum(finish_num) as finish_num, sum(marked_num) as marked_num \n" +
                "from v_task_progress v \n" +
                "where company_id = #{serviceHeader.companyId} \n" );

        if(reqs != null && reqs.size() > 0){
            sb.append("and (\n");
            for(int i = 0; i < reqs.size(); i++){
                sb.append("(creater = #{reqs[" + i + "].account} and task_id = #{reqs[" + i + "].taskId} and project_id = #{reqs[" + i + "].projectId})\n");
                if(i < (reqs.size() - 1))
                    sb.append("or \n");
            }
            sb.append(")\n");
        }
        sb.append("group by project_id, task_id, creater) a \n" +
                "left join t_task t on a.task_id = t.id \n" +
                "left join t_project p on a.project_id = p.id");

        return sb.toString();
    }

}
