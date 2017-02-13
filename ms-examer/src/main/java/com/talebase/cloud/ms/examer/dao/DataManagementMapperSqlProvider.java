package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.dto.DDataManagementRequest;
import com.talebase.cloud.common.protocal.PageRequest;

/**
 * Created by daorong.li on 2016-12-19.
 */
public class DataManagementMapperSqlProvider {

    public String query(PageRequest pageRequest, Integer companyId, DDataManagementRequest dDataManagementRequest) {
        String sql = "select id,account,name,gender,email,mobile,total_score,end_time,whetherMarkDone," +
                "case when used_time >= 1440 and used_time <= 2880 then '2天' " +
                " when used_time > 2880 and used_time <= 4320 then '3天' " +
                " when used_time >4320 then concat('3天以上,',TIMESTAMPDIFF(DAY,start_time,end_time),'天以内') else concat(used_time,'分钟') end used_time from (" +
                " SELECT" +
                " ui.id,ui.account,ui.`name`,ui.gender,ui.email,ui.mobile,TIMESTAMPDIFF(MINUTE,e.start_time,e.end_time)used_time ," +
                " e.start_time,e.end_time," +
                "CASE WHEN (e.obj_score IS NOT NULL and e.sub_score IS NOT NULL) THEN e.obj_score + e.sub_score WHEN (e.obj_score IS NOT NULL and e.sub_score IS NULL) THEN e.obj_score WHEN (e.obj_score IS NULL and e.sub_score IS NOT NULL) THEN e.sub_score ELSE -1 END total_score," +
                " case when e.sub_score is null then 0 else 1 end whetherMarkDone" +
                " FROM t_user_info ui" +
                " JOIN t_user_exam ue ON ue.user_id = ui.id" +
                " LEFT JOIN t_exercise e on ui.id = e.user_id and e.task_id = ue.task_id" +
                " WHERE ue.company_id = #{companyId}" +
                " AND ue.project_id = #{dDataManagementRequest.projectId}" +
                " and ue.task_id = #{dDataManagementRequest.taskId}" +
                " )a ";
        if (pageRequest.getSortFields() != null) {
            sql += " order by " + pageRequest.getSortFields();
        }
        sql += " limit " + pageRequest.getStart() + "," + pageRequest.getLimit() + "";
        return sql;
    }

    public String queryCount(Integer companyId, DDataManagementRequest dDataManagementRequest) {
        String sql = "select count(id) cnt from (" +
                " SELECT" +
                " ui.id,ui.account,ui.`name`,ui.gender,ui.email,ui.mobile,TIMESTAMPDIFF(MINUTE,e.start_time,e.end_time)used_time ," +
                " e.end_time,e.obj_score + e.sub_score total_score," +
                " case when (e.sub_score is null or e.sub_score = 0) then 0 else 1 end whetherMarkDone" +
                " FROM t_user_info ui" +
                " JOIN t_user_exam ue ON ue.user_id = ui.id" +
                " LEFT JOIN t_exercise e on ui.id = e.user_id and e.task_id = ue.task_id" +
                " WHERE ue.company_id = #{companyId}" +
                " AND ue.project_id = #{dDataManagementRequest.projectId}" +
                " and ue.task_id = #{dDataManagementRequest.taskId}" +
                " )a ";
        return sql;
    }
}
