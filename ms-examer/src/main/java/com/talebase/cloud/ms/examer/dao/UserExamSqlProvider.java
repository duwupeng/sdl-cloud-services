package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.dto.DReSetPassword;
import com.talebase.cloud.base.ms.examer.enums.TUserExamStatus;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.base.ms.project.domain.TProject;
import com.talebase.cloud.common.util.NumberUtil;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-9.
 */
public class UserExamSqlProvider {

    public String getCntForExamAndUser(Integer userId, Integer projectId, Integer taskId) {
        return new SQL() {{
            SELECT("count(*)");
            FROM("t_user_exam");
            WHERE("user_id = #{userId}");
            WHERE("project_id = #{projectId}");
            WHERE("task_id = #{taskId}");
            WHERE("status not in (" + TUserExamStatus.DELETE.getValue() + ")");
        }}.toString();
    }

    public String getCntForExamAndUserByMobileAndProjectId(String mobile, Integer projectId) {
        return new SQL() {{
            SELECT("count(*)");
            FROM("t_user_exam ue");
            INNER_JOIN("t_user_info ui on ue.user_id = ui.id");
            WHERE("ui.mobile = #{mobile}");
            WHERE("ue.project_id = #{projectId}");
            WHERE("ue.status not in (" + TUserExamStatus.DELETE.getValue() + ")");
        }}.toString();
    }

    public String getUserExam(Integer userId) {
        return new SQL() {{
            SELECT("*");
            FROM("t_user_exam");
            WHERE("user_id = #{userId}");
            WHERE("status not in (" + TUserExamStatus.DELETE.getValue() + ")");
        }}.toString();
    }

    public String insert(TUserExam userExam) {
        return new SQL() {{
            INSERT_INTO("t_user_exam");
            VALUES("type", "#{type}");
            VALUES("user_id", "#{userId}");
            VALUES("project_id", "#{projectId}");
            VALUES("task_id", "#{taskId}");
            VALUES("status", "" + TUserExamStatus.ENABLED.getValue());
            VALUES("create_time", "now()");
            VALUES("creater", "#{creater}");
            VALUES("company_id", "#{companyId}");
        }}.toString();
    }

    public String updateStatus(DReSetPassword data, Integer status, List<Integer> idList) {
        return new SQL() {{
            UPDATE("t_user_exam");
            SET(" status = #{status}");
            WHERE(" company_id =#{data.companyId} ");
            WHERE(" project_id =#{data.projectId} ");
            WHERE(" task_id = #{data.taskId} ");
            if (idList != null && idList.size() > 0) {
                WHERE(" user_id " + SqlBuilderUtil.buildInSql("idList", idList.size()));
            }
        }}.toString();
    }

    public String updateProjectById(TProject data) {
        return new SQL() {{
            UPDATE("t_project");
            SET(" scan_now = #{data.scanNow}");
            WHERE(" id = #{data.id} ");
        }}.toString();
    }


    public String delExamer(DReSetPassword data, List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("update t_user_exam e ")
                .append(" inner join t_user_info a on a.id = e.user_id ")
                .append(" left join t_exercise t on t.user_id = e.user_id and t.task_id = e.task_id ")
                .append(" set e.status =" + TUserExamStatus.DELETE.getValue())
                .append(" where e.project_id =#{data.projectId} and e.task_id = #{data.taskId} ")
                .append(" and e.company_id = #{data.companyId} ")
                .append(" and e.status > " + TUserExamStatus.DELETE.getValue());
        //创建时间
        if (data.isCreateTime_begin()) {
            sb.append(" and  e.create_time >= #{data.key1Begin} ");
        }
        if (data.isCreateTime_end()) {
            sb.append(" and  e.create_time <= #{data.key1End}");
        }
        if (data.isBirthday_begin()) {//查询出生日期
            sb.append(" and  a.birthday >= #{data.key1Begin} ");
        }
        if (data.isBirthday_end()) {
            sb.append(" and  a.birthday <= #{data.key1End} ");
        }
        //查询账号
        if (data.isAccount1()) {
            sb.append(" and a.account like  concat('%', #{data.key2}, '%') ");
        } else if (data.isEmail()) {
            sb.append(" and a.email  like  concat('%', #{data.key2}, '%') ");
        } else if (data.isName()) {
            sb.append(" and a.name  like  concat('%', #{data.key2}, '%') ");
        } else if (data.isMobile()) {
            sb.append(" and a.mobile like  concat('%', #{data.key2}, '%') ");
        }

        //通信状态
        if (!StringUtils.isEmpty(data.getCmctStatus())) {
            String cmcStatusStr = data.getCmctStatus();
            if (!(cmcStatusStr.contains(data.CMCSTATUS_EMAIL_SEND) && cmcStatusStr.contains(data.CMCSTATUS_EMAIL_NO))) {
                if (cmcStatusStr.contains(data.CMCSTATUS_EMAIL_SEND))
                    sb.append(" and e.send_email_status is not null ");
                else if (cmcStatusStr.contains(data.CMCSTATUS_EMAIL_NO))
                    sb.append(" and e.send_email_status is null ");
            }

            if (!(cmcStatusStr.contains(data.CMCSTATUS_SMS) && cmcStatusStr.contains(data.CMCSTATUS_SMS_NO))) {
                if (cmcStatusStr.contains(data.CMCSTATUS_SMS))
                    sb.append(" and e.send_sms_status is not null ");
                else if (cmcStatusStr.contains(data.CMCSTATUS_SMS_NO))
                    sb.append(" and e.send_sms_status is null ");
            }
            /*String[] cmctStatus = data.getCmctStatus().split(",");
            for (int i=0;i<cmctStatus.length;i++){
                if (data.CMCSTATUS_EMAIL_SEND.equals(cmctStatus[i])){
                    sb.append(" and e.send_email_status is not null ");
                }else if (data.CMCSTATUS_EMAIL_NO.equals(cmctStatus[i])){
                    sb.append(" and e.send_email_status is null ");
                }

                if (data.CMCSTATUS_SMS.equals(cmctStatus[i])){
                    sb.append(" and e.send_sms_status is not null ");
                }else if (data.CMCSTATUS_SMS_NO.equals(cmctStatus[i])){
                    sb.append(" and e.send_sms_status is null ");
                }
            }*/
        }
        //通讯方式
        if (!StringUtils.isEmpty(data.getCmctWay())) {
            String cmcWayStr = data.getCmctWay();
            if (!(cmcWayStr.contains(data.CMCWAY_EMAIL) && cmcWayStr.contains(data.CMCWAY_EMAIL_NO))) {
                if (cmcWayStr.contains(data.CMCWAY_EMAIL))
                    sb.append(" and a.email is not null ");
                else if (cmcWayStr.contains(data.CMCWAY_EMAIL_NO))
                    sb.append(" and a.email is null ");
            }
            if (!(cmcWayStr.contains(data.CMCWAY_SMS) && cmcWayStr.contains(data.CMCWAY_SMS_NO))) {
                if (cmcWayStr.contains(data.CMCWAY_SMS))
                    sb.append(" and a.mobile is not null ");
                else if (cmcWayStr.contains(data.CMCWAY_SMS_NO))
                    sb.append(" and a.mobile is null ");
            }

            /*String[] cmcWay = data.getCmctWay().split(",");
            for (int i=0;i<cmcWay.length;i++){
                if (data.CMCWAY_EMAIL.equals(cmcWay[i])){
                    sb.append(" and a.email is not null ");
                }else if (data.CMCWAY_EMAIL_NO.equals(cmcWay[i])){
                    sb.append(" and a.email is null ");
                }
                if (data.CMCWAY_SMS.equals(cmcWay[i])){
                    sb.append(" and a.mobile is not null ");
                }else if (data.CMCWAY_SMS_NO.equals(cmcWay[i])){
                    sb.append(" and a.mobile is null ");
                }
            }*/
        }
        if (ids != null && ids.size() > 0) {
            sb.append(" and e.user_id " + SqlBuilderUtil.buildInSql("ids", ids.size()));
        }
        //答题状态
        if (!StringUtils.isEmpty(data.getStatus())) {
            String[] status = data.getStatus().split(",");
            String statusStr = data.getStatus();
            //全选则不查询
            if (!(statusStr.contains(data.NOBEGIN.toString())
                    && statusStr.contains(data.ANSWER.toString())
                    && statusStr.contains(data.COMPLETE.toString()))) {
                sb.append(" and ( 1=2 ");
                for (int i = 0; i < status.length; i++) {
                    if (data.NOBEGIN.equals(Integer.valueOf(status[i]))) { //未开始
                        sb.append(" or t.start_time is null ");
                    } else if (data.ANSWER.equals(Integer.valueOf(status[i]))) {//答题中
                        sb.append(" or (t.start_time is not null and t.end_time is null) ");
                    } else if (data.COMPLETE.equals(Integer.valueOf(status[i]))) {//已完成
                        sb.append(" or t.end_time is not null ");
                    }
                }
                sb.append(" )");
            }
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    public String queryStartExamCnt(DReSetPassword data, List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from t_user_exam e ")
                .append(" inner join t_user_info a on a.id = e.user_id ")
                .append(" left join t_exercise t on t.user_id = e.user_id and t.task_id = e.task_id ")
                .append(" where e.project_id =#{data.projectId} and e.task_id = #{data.taskId} ")
                .append(" and e.company_id = #{data.companyId} ")
                .append(" and e.status > " + TUserExamStatus.DELETE.getValue() + "")
                .append(" and t.start_time is not null ");
        //创建时间

        if (data.isCreateTime_begin()) {
            sb.append(" and  e.create_time >= #{data.key1Begin} ");
        }
        if (data.isCreateTime_end()) {
            sb.append(" and  e.create_time <= #{data.key1End}");
        }
        if (data.isBirthday_begin()) {//查询出生日期
            sb.append(" and  a.birthday >= #{data.key1Begin} ");
        }
        if (data.isBirthday_end()) {
            sb.append(" and  a.birthday <= #{data.key1End} ");
        }
        //查询账号
        if (data.isAccount1()) {
            sb.append(" and a.account like  concat('%', #{data.key2}, '%') ");
        } else if (data.isEmail()) {
            sb.append(" and a.email  like  concat('%', #{data.key2}, '%') ");
        } else if (data.isName()) {
            sb.append(" and a.name  like  concat('%', #{data.key2}, '%') ");
        } else if (data.isMobile()) {
            sb.append(" and a.mobile like  concat('%', #{data.key2}, '%') ");
        }

        //通信状态
        if (!StringUtils.isEmpty(data.getCmctStatus())) {
            String cmcStatusStr = data.getCmctStatus();
            if (!(cmcStatusStr.contains(data.CMCSTATUS_EMAIL_SEND) && cmcStatusStr.contains(data.CMCSTATUS_EMAIL_NO))) {
                if (cmcStatusStr.contains(data.CMCSTATUS_EMAIL_SEND))
                    sb.append(" and e.send_email_status is not null ");
                else if (cmcStatusStr.contains(data.CMCSTATUS_EMAIL_NO))
                    sb.append(" and e.send_email_status is null ");
            }

            if (!(cmcStatusStr.contains(data.CMCSTATUS_SMS) && cmcStatusStr.contains(data.CMCSTATUS_SMS_NO))) {
                if (cmcStatusStr.contains(data.CMCSTATUS_SMS))
                    sb.append(" and e.send_sms_status is not null ");
                else if (cmcStatusStr.contains(data.CMCSTATUS_SMS_NO))
                    sb.append(" and e.send_sms_status is null ");
            }
            /*String[] cmctStatus = data.getCmctStatus().split(",");
            for (int i=0;i<cmctStatus.length;i++){
                if (data.CMCSTATUS_EMAIL_SEND.equals(cmctStatus[i])){
                    sb.append(" and e.send_email_status is not null ");
                }else if (data.CMCSTATUS_EMAIL_NO.equals(cmctStatus[i])){
                    sb.append(" and e.send_email_status is null ");
                }

                if (data.CMCSTATUS_SMS.equals(cmctStatus[i])){
                    sb.append(" and e.send_sms_status is not null ");
                }else if (data.CMCSTATUS_SMS_NO.equals(cmctStatus[i])){
                    sb.append(" and e.send_sms_status is null ");
                }
            }*/
        }
        //通讯方式
        if (!StringUtils.isEmpty(data.getCmctWay())) {
            String cmcWayStr = data.getCmctWay();
            if (!(cmcWayStr.contains(data.CMCWAY_EMAIL) && cmcWayStr.contains(data.CMCWAY_EMAIL_NO))) {
                if (cmcWayStr.contains(data.CMCWAY_EMAIL))
                    sb.append(" and a.email is not null ");
                else if (cmcWayStr.contains(data.CMCWAY_EMAIL_NO))
                    sb.append(" and a.email is null ");
            }
            if (!(cmcWayStr.contains(data.CMCWAY_SMS) && cmcWayStr.contains(data.CMCWAY_SMS_NO))) {
                if (cmcWayStr.contains(data.CMCWAY_SMS))
                    sb.append(" and a.mobile is not null ");
                else if (cmcWayStr.contains(data.CMCWAY_SMS_NO))
                    sb.append(" and a.mobile is null ");
            }

            /*String[] cmcWay = data.getCmctWay().split(",");
            for (int i=0;i<cmcWay.length;i++){
                if (data.CMCWAY_EMAIL.equals(cmcWay[i])){
                    sb.append(" and a.email is not null ");
                }else if (data.CMCWAY_EMAIL_NO.equals(cmcWay[i])){
                    sb.append(" and a.email is null ");
                }
                if (data.CMCWAY_SMS.equals(cmcWay[i])){
                    sb.append(" and a.mobile is not null ");
                }else if (data.CMCWAY_SMS_NO.equals(cmcWay[i])){
                    sb.append(" and a.mobile is null ");
                }
            }*/
        }
        if (ids != null && ids.size() > 0) {
            sb.append(" and e.user_id " + SqlBuilderUtil.buildInSql("ids", ids.size()));
        }
        //答题状态
        if (!StringUtils.isEmpty(data.getStatus())) {
            String[] status = data.getStatus().split(",");
            String statusStr = data.getStatus();
            //全选则不查询
            if (!(statusStr.contains(data.NOBEGIN.toString())
                    && statusStr.contains(data.ANSWER.toString())
                    && statusStr.contains(data.COMPLETE.toString()))) {
                sb.append(" and ( 1=2 ");
                for (int i = 0; i < status.length; i++) {
                    if (data.NOBEGIN.equals(Integer.valueOf(status[i]))) { //未开始
                        sb.append(" or t.start_time is null ");
                    } else if (data.ANSWER.equals(Integer.valueOf(status[i]))) {//答题中
                        sb.append(" or (t.start_time is not null and t.end_time is null) ");
                    } else if (data.COMPLETE.equals(Integer.valueOf(status[i]))) {//已完成
                        sb.append(" or t.end_time is not null ");
                    }
                }
                sb.append(" )");
            }
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    public String delStartExamExercise(DReSetPassword data, List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append(" delete t from  t_exercise t ")
                .append(" inner join t_user_exam e on t.user_id = e.user_id and t.task_id = e.task_id")
                .append(" inner join t_user_info a on a.id = e.user_id ")
                .append(" where e.project_id =#{data.projectId} and e.task_id = #{data.taskId} ")
                .append(" and e.company_id = #{data.companyId} ")
                .append(" and e.status > " + TUserExamStatus.DELETE.getValue() + "")
                .append(" and t.start_time is not null ");
        //创建时间

        if (data.isCreateTime_begin()) {
            sb.append(" and  e.create_time >= #{data.key1Begin} ");
        }
        if (data.isCreateTime_end()) {
            sb.append(" and  e.create_time <= #{data.key1End}");
        }
        if (data.isBirthday_begin()) {//查询出生日期
            sb.append(" and  a.birthday >= #{data.key1Begin} ");
        }
        if (data.isBirthday_end()) {
            sb.append(" and  a.birthday <= #{data.key1End} ");
        }
        //查询账号
        if (data.isAccount1()) {
            sb.append(" and a.account like  concat('%', #{data.key2}, '%') ");
        } else if (data.isEmail()) {
            sb.append(" and a.email  like  concat('%', #{data.key2}, '%') ");
        } else if (data.isName()) {
            sb.append(" and a.name  like  concat('%', #{data.key2}, '%') ");
        } else if (data.isMobile()) {
            sb.append(" and a.mobile like  concat('%', #{data.key2}, '%') ");
        }
        //通信状态
        if (!StringUtils.isEmpty(data.getCmctStatus())) {
            String cmcStatusStr = data.getCmctStatus();
            if (!(cmcStatusStr.contains(data.CMCSTATUS_EMAIL_SEND) && cmcStatusStr.contains(data.CMCSTATUS_EMAIL_NO))) {
                if (cmcStatusStr.contains(data.CMCSTATUS_EMAIL_SEND))
                    sb.append(" and e.send_email_status is not null ");
                else if (cmcStatusStr.contains(data.CMCSTATUS_EMAIL_NO))
                    sb.append(" and e.send_email_status is null ");
            }

            if (!(cmcStatusStr.contains(data.CMCSTATUS_SMS) && cmcStatusStr.contains(data.CMCSTATUS_SMS_NO))) {
                if (cmcStatusStr.contains(data.CMCSTATUS_SMS))
                    sb.append(" and e.send_sms_status is not null ");
                else if (cmcStatusStr.contains(data.CMCSTATUS_SMS_NO))
                    sb.append(" and e.send_sms_status is null ");
            }
            /*String[] cmctStatus = data.getCmctStatus().split(",");
            for (int i=0;i<cmctStatus.length;i++){
                if (data.CMCSTATUS_EMAIL_SEND.equals(cmctStatus[i])){
                    sb.append(" and e.send_email_status is not null ");
                }else if (data.CMCSTATUS_EMAIL_NO.equals(cmctStatus[i])){
                    sb.append(" and e.send_email_status is null ");
                }

                if (data.CMCSTATUS_SMS.equals(cmctStatus[i])){
                    sb.append(" and e.send_sms_status is not null ");
                }else if (data.CMCSTATUS_SMS_NO.equals(cmctStatus[i])){
                    sb.append(" and e.send_sms_status is null ");
                }
            }*/
        }
        //通讯方式
        if (!StringUtils.isEmpty(data.getCmctWay())) {
            String cmcWayStr = data.getCmctWay();
            if (!(cmcWayStr.contains(data.CMCWAY_EMAIL) && cmcWayStr.contains(data.CMCWAY_EMAIL_NO))) {
                if (cmcWayStr.contains(data.CMCWAY_EMAIL))
                    sb.append(" and a.email is not null ");
                else if (cmcWayStr.contains(data.CMCWAY_EMAIL_NO))
                    sb.append(" and a.email is null ");
            }
            if (!(cmcWayStr.contains(data.CMCWAY_SMS) && cmcWayStr.contains(data.CMCWAY_SMS_NO))) {
                if (cmcWayStr.contains(data.CMCWAY_SMS))
                    sb.append(" and a.mobile is not null ");
                else if (cmcWayStr.contains(data.CMCWAY_SMS_NO))
                    sb.append(" and a.mobile is null ");
            }

            /*String[] cmcWay = data.getCmctWay().split(",");
            for (int i=0;i<cmcWay.length;i++){
                if (data.CMCWAY_EMAIL.equals(cmcWay[i])){
                    sb.append(" and a.email is not null ");
                }else if (data.CMCWAY_EMAIL_NO.equals(cmcWay[i])){
                    sb.append(" and a.email is null ");
                }
                if (data.CMCWAY_SMS.equals(cmcWay[i])){
                    sb.append(" and a.mobile is not null ");
                }else if (data.CMCWAY_SMS_NO.equals(cmcWay[i])){
                    sb.append(" and a.mobile is null ");
                }
            }*/
        }
        if (ids != null && ids.size() > 0) {
            sb.append(" and e.user_id " + SqlBuilderUtil.buildInSql("ids", ids.size()));
        }
        //答题状态
        if (!StringUtils.isEmpty(data.getStatus())) {
            String[] status = data.getStatus().split(",");
            String statusStr = data.getStatus();
            //全选则不查询
            if (!(statusStr.contains(data.NOBEGIN.toString())
                    && statusStr.contains(data.ANSWER.toString())
                    && statusStr.contains(data.COMPLETE.toString()))) {
                sb.append(" and ( 1=2 ");
                for (int i = 0; i < status.length; i++) {
                    if (data.NOBEGIN.equals(Integer.valueOf(status[i]))) { //未开始
                        sb.append(" or t.start_time is null ");
                    } else if (data.ANSWER.equals(Integer.valueOf(status[i]))) {//答题中
                        sb.append(" or (t.start_time is not null and t.end_time is null) ");
                    } else if (data.COMPLETE.equals(Integer.valueOf(status[i]))) {//已完成
                        sb.append(" or t.end_time is not null ");
                    }
                }
                sb.append(" )");
            }
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    public String getUserExamPermission(Integer taskId, Integer userId) {
        return "select t.id as task_id, t.name as task_name, t.status as task_status, t.paper_id as task_paper_id, " +
                "t.start_date as task_start_date, t.end_date as task_end_date, t.exam_time as task_exam_time, t.latest_start_date as task_latest_start_date," +
                "t.page_change_limit as task_page_change_limit, " +
                "p.id as project_id, p.name as project_name, p.status as project_status, p.creater as project_admin," +
                "ue.user_id as user_id, ue.status as user_exam_status, ue.creater as examer_admin," +
                "e.paper_id as exercise_paper_id, e.start_time as exercise_start_time, e.end_time as exercise_end_time " +
                "from t_task t " +
                "join t_project p on t.project_id = p.id " +
                "join t_user_exam ue on t.id = ue.task_id " +
                "left join t_exercise e on t.id = e.task_id and ue.user_id = e.user_id " +
                "where t.id = #{taskId} " +
                "and ue.user_id = #{userId} " +
                "and ue.status not in (" + TUserExamStatus.DELETE.getValue() + ") ";
    }

    public String updateSendStatus(Integer projectId, Integer taskId, String account, Integer type, Integer status) {
        return new SQL() {{
            UPDATE("t_user_exam");
            if (type == TNotifyTemplateMethod.MAIL.getValue()) {
                SET("send_email_status = #{status}");
            } else if (type == TNotifyTemplateMethod.SMS.getValue()) {
                SET("send_sms_status = #{status}");
            }
            WHERE("project_id = #{projectId}");
            WHERE("task_id = #{taskId}");
            WHERE("user_id = (select id from t_user_info where account = #{account} limit 1)");
        }}.toString();
    }

    public String getUserExamInfos(Integer taskId) {
        String sql = "select u.id, u.name, u.account from t_user_exam ue " +
                "join t_user_info u on ue.user_id = u.id " +
                "join t_exercise e on ue.task_id = e.task_id and ue.user_id = e.user_id " +
                "where ue.status = " + TUserExamStatus.ENABLED.getValue() + "" +
                "and ue.task_id = #{taskId} ";
        return sql;
    }


    public String getTaksCountToNotify(Integer companyId, Integer userId, Integer projectId, Integer taskId) {
        return new SQL() {{
            SELECT("count(*) cnt");
            FROM("v_task_progress");
            WHERE("user_id = #{userId}");
            WHERE("company_id = #{companyId}");
            WHERE("finish_num = 0");
            if (!NumberUtil.isEmpty(projectId)) {
                WHERE("project_id = #{projectId}");
            }
            if (!NumberUtil.isEmpty(taskId)) {
                WHERE("task_id = #{taskId}");
            }
        }}.toString();
    }

    public String getTaksListToNotify(Integer companyId, Integer userId, Integer projectId, Integer taskId) {
        String param = "";
        if (NumberUtil.isEmpty(projectId)) {
            param += " and v.project_id = #{projectId}";
        }
        if (NumberUtil.isEmpty(taskId)) {
            param += " and v.task_id = #{taskId}";
        }
        String sql = "select projectName,taskName,paper_num,paper_id,start_date,latest_start_date," +
                " case when exam_time is null or exam_time = 0 then end_date ELSE (" +
                " case when minutes >= 1440 and minutes <= 2880 then '2天' " +
                " when minutes > 2880 and minutes <= 4320 then '3天' " +
                " when minutes >4320 then concat('3天以上,',TIMESTAMPDIFF(DAY,start_date,end_date),'天以内')  else concat(minutes,'分钟') end) end duration  from (" +
                " select p.name projectName,t.`name` taskName,t.paper_num,t.paper_id,t.start_date,t.latest_start_date,t.end_date," +
                " TIMESTAMPDIFF(MINUTE,t.start_date,t.end_date) minutes,exam_time" +
                " from v_task_progress v " +
                " JOIN t_project p on v.project_id = p.id" +
                " JOIN t_task t on v.task_id = t.id" +
                " where v.finish_num = 0" +
                " and user_id = #{userId}" +
                " and v.company_id = #{companyId}" +
                // " and t.end_date < now()" +
                param +
                " )a";
        return sql;
    }


}
