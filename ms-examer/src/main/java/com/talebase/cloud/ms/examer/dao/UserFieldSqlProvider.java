package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.base.ms.examer.dto.DUserExamPageRequest;
import com.talebase.cloud.base.ms.examer.dto.DUserShowField;
import com.talebase.cloud.base.ms.examer.enums.TUserExamStatus;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-7.
 */
public class UserFieldSqlProvider {

    public String buildInsertSql(DUserShowField dUserShowField){
        return new SQL(){{
            INSERT_INTO("t_user_show_field");
            VALUES("field_name", "#{fieldName}");
            VALUES("field_key", "#{fieldKey}");
            VALUES("isshow",  "#{isshow}");
            VALUES("ismandatory",  "#{ismandatory}");
            VALUES("isunique",  "#{isunique}");
            VALUES("task_id",  "#{taskId}");
            VALUES("project_id",  "#{projectId}");
            VALUES("isextension",  "#{isextension}");
            VALUES("sortnum",  "#{sortnum}");
            VALUES("company_id",  "#{companyId}");
            VALUES("create_date",  "now()");
            VALUES("creater",  "#{creater}");
            VALUES("type",  "#{type}");
            VALUES("select_value",  "#{selectValue}");
        }}.toString();
    }

    public String iniCompanyUserField(Integer companyId){
        return "INSERT INTO t_user_show_field (field_name, field_key, isshow, ismandatory," +
                "isunique, isextension, sortnum, company_id, creater, type, select_value, modifier) " +
                "select field_name, field_key, isshow, ismandatory," +
                "isunique, isextension, sortnum, "+companyId+", creater, type, select_value, modifier " +
                "from t_user_show_field where company_id = 0;";
    }

    public String buildUpdateByGlobalKeySql(DUserShowField dUserShowField){
        return new SQL(){{
            UPDATE("t_user_show_field");
            if (!StringUtils.isEmpty(dUserShowField.getIsshow()))
                SET("isshow = #{isshow}");
            if (!StringUtils.isEmpty(dUserShowField.getIsmandatory()))
                SET("ismandatory = #{ismandatory}");
            if (!StringUtils.isEmpty(dUserShowField.getIsunique()))
                SET("isunique = #{isunique}");
            SET("modifier = #{modifier}");
            SET("modify_date = now()");
            WHERE(" field_key = #{fieldKey}  ");
            AND();
            WHERE(" company_id = #{companyId} ");
            AND();
            WHERE(" project_id IS NULL AND task_id IS NULL ");
        }}.toString();
    }

    public String findExamers(Integer companyId, Integer projectId, Integer taskId){
        return new SQL(){{
            SELECT("*");
            FROM("t_user_show_field");
            WHERE("company_id = #{companyId}");

            if(projectId != null && projectId > 0){
                WHERE("project_id = #{projectId}");
            }else{
                WHERE("project_id is null");
            }

            if(taskId != null && taskId > 0){
                WHERE("task_id = #{taskId}");
            }else{
                WHERE("task_id is null");
            }
            ORDER_BY(" isextension asc ,sortnum asc, id asc");
        }}.toString();
    }

    public String getCountProjectExamersSql(DUserExamPageRequest data){
        String sql ="select count(*) total from ("+getProjectExamersSql(data,null)+")t";
        return sql;
    }

    public String getProjectExamersPageSql(DUserExamPageRequest data,Integer start,Integer limit){
        String sql = getProjectExamersSql(data,null)+"  limit #{start},#{limit} ";
        return sql;
    }

    public String getProjectExamersSql(DUserExamPageRequest data,List<Integer> ids){
        StringBuilder sb  = new StringBuilder();
        sb.append(" select    \n")
                .append(" e.*,a.account,a.name,a.email,a.mobile,a.gender,a.password,a.highest_education,a.identity_num,\n ")
                .append(" a.birthday ,a.work_years,a.industry_name,a.political_status,a.dept_id,a.dept_name, \n")
                .append(" a.position,a.school,a.profession,a.status userStatus,a.extension_field, \n")
                .append(" t.start_time as exam_start_time,t.end_time as exam_finished_time \n")
                .append(" from t_user_exam e \n")
                .append(" inner join t_user_info a on a.id = e.user_id \n")
                .append(" left join t_exercise t on t.user_id = e.user_id and t.task_id = e.task_id \n")
                .append(" where e.project_id =#{data.projectId} and e.task_id = #{data.taskId} \n")
                .append(" and e.company_id = #{data.companyId} \n")
                .append(" and e.status > "+ TUserExamStatus.DELETE.getValue());
        //创建时间
        if (data.isCreateTime_begin()){
            sb.append(" and  e.create_time >= #{data.key1Begin} \n");
        }
        if (data.isCreateTime_end()){
            sb.append(" and  e.create_time <= #{data.key1End} \n");
        }
        if (data.isBirthday_begin()){//查询出生日期
            sb.append(" and  a.birthday >= #{data.key1Begin} \n");
        }
        if (data.isBirthday_end()){
            sb.append(" and  a.birthday <= #{data.key1End} \n");
        }
        //查询账号
        if (data.isAccount1()){
            sb.append(" and a.account like  concat('%', #{data.key2}, '%') \n");
        }else if (data.isEmail()){
            sb.append(" and a.email  like  concat('%', #{data.key2}, '%') \n");
        }else if (data.isName()){
            sb.append(" and a.name  like  concat('%', #{data.key2}, '%') \n");
        }else if (data.isMobile()){
            sb.append(" and a.mobile like  concat('%', #{data.key2}, '%') \n");
        }
        //通信状态
        if (!StringUtils.isEmpty(data.getCmctStatus())){
            String cmcStatusStr = data.getCmctStatus();
            if (!(cmcStatusStr.contains(data.CMCSTATUS_EMAIL_SEND) && cmcStatusStr.contains(data.CMCSTATUS_EMAIL_NO))){
                if (cmcStatusStr.contains(data.CMCSTATUS_EMAIL_SEND))
                    sb.append(" and e.send_email_status is not null \n");
                else if (cmcStatusStr.contains(data.CMCSTATUS_EMAIL_NO))
                    sb.append(" and e.send_email_status is null \n");
            }

            if (!(cmcStatusStr.contains(data.CMCSTATUS_SMS) && cmcStatusStr.contains(data.CMCSTATUS_SMS_NO))){
                if (cmcStatusStr.contains(data.CMCSTATUS_SMS))
                    sb.append(" and e.send_sms_status is not null \n");
                else if (cmcStatusStr.contains(data.CMCSTATUS_SMS_NO))
                     sb.append(" and e.send_sms_status is null \n");
            }
            /*String[] cmctStatus = data.getCmctStatus().split(",");
            for (int i=0;i<cmctStatus.length;i++){
                if (data.CMCSTATUS_EMAIL_SEND.equals(cmctStatus[i])){
                    sb.append(" and e.send_email_status is not null \n");
                }else if (data.CMCSTATUS_EMAIL_NO.equals(cmctStatus[i])){
                    sb.append(" and e.send_email_status is null \n");
                }

                if (data.CMCSTATUS_SMS.equals(cmctStatus[i])){
                    sb.append(" and e.send_sms_status is not null \n");
                }else if (data.CMCSTATUS_SMS_NO.equals(cmctStatus[i])){
                    sb.append(" and e.send_sms_status is null \n");
                }
            }*/
        }
        //通讯方式
        if (!StringUtils.isEmpty(data.getCmctWay())){
            String cmcWayStr = data.getCmctWay();
            if (!(cmcWayStr.contains(data.CMCWAY_EMAIL) && cmcWayStr.contains(data.CMCWAY_EMAIL_NO))){
                if (cmcWayStr.contains(data.CMCWAY_EMAIL))
                    sb.append(" and a.email is not null \n");
                else if (cmcWayStr.contains(data.CMCWAY_EMAIL_NO))
                     sb.append(" and a.email is null \n");
            }
            if (!(cmcWayStr.contains(data.CMCWAY_SMS) && cmcWayStr.contains(data.CMCWAY_SMS_NO))){
                if (cmcWayStr.contains(data.CMCWAY_SMS))
                    sb.append(" and a.mobile is not null \n");
                else if (cmcWayStr.contains(data.CMCWAY_SMS_NO))
                    sb.append(" and a.mobile is null \n");
            }

            /*String[] cmcWay = data.getCmctWay().split(",");
            for (int i=0;i<cmcWay.length;i++){
                if (data.CMCWAY_EMAIL.equals(cmcWay[i])){
                    sb.append(" and a.email is not null \n");
                }else if (data.CMCWAY_EMAIL_NO.equals(cmcWay[i])){
                    sb.append(" and a.email is null \n");
                }
                if (data.CMCWAY_SMS.equals(cmcWay[i])){
                    sb.append(" and a.mobile is not null \n");
                }else if (data.CMCWAY_SMS_NO.equals(cmcWay[i])){
                    sb.append(" and a.mobile is null \n");
                }
            }*/
        }
        if (ids != null && ids.size()>0){
            sb.append(" and e.user_id " +SqlBuilderUtil.buildInSql("ids", ids.size()));
        }
        //答题状态
        if (!StringUtils.isEmpty(data.getStatus())){
            String[] status = data.getStatus().split(",");
            String statusStr = data.getStatus();
            //全选则不加查询条件
            if (!(statusStr.contains(data.NOBEGIN.toString())
                    && statusStr.contains(data.ANSWER.toString())
                    && statusStr.contains(data.COMPLETE.toString()))){
                sb.append(" and ( 1=2 ");
                for (int i=0;i<status.length;i++){
                    if (data.NOBEGIN.equals(Integer.valueOf(status[i]))){ //未开始
                        sb.append(" or t.start_time is null \n");
                    }else if (data.ANSWER.equals(Integer.valueOf(status[i]))){//答题中
                        sb.append(" or (t.start_time is not null and t.end_time is null) \n");
                    }else if (data.COMPLETE.equals(Integer.valueOf(status[i]))){//已完成
                        sb.append(" or t.end_time is not null \n");
                    }
                }
                sb.append(" )");
            }
        }
        sb.append(" order by e.create_time desc ");
        //System.out.println(sb.toString());
        return  sb.toString();
    }

}
