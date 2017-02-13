package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.dto.DReSetPassword;
import com.talebase.cloud.base.ms.examer.dto.DUpdatePassword;
import com.talebase.cloud.base.ms.examer.dto.DUserExamPageRequest;
import com.talebase.cloud.base.ms.examer.enums.TUserExamStatus;
import com.talebase.cloud.base.ms.examer.enums.TUserInfoStatus;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
public class UserInfoSqlProvider {

    public String insert(TUserInfo userInfo) {
        return new SQL() {{
            INSERT_INTO("t_user_info");
            VALUES("account", "#{account}");
            VALUES("mobile", "#{mobile}");
            VALUES("password", "#{password}");
            VALUES("status", "" + TUserInfoStatus.ENABLED.getValue());
            VALUES("company_id", "#{companyId}");
            VALUES("create_date", "now()");
            VALUES("creater", "#{creater}");

            if (userInfo.getName() != null)
                VALUES("name", "#{name}");
            if (userInfo.getEmail() != null)
                VALUES("email", "#{email}");
            if (userInfo.getGender() != null)
                VALUES("gender", "#{gender}");
            if (userInfo.getHighestEducation() != null)
                VALUES("highest_education", "#{highestEducation}");
            if (userInfo.getIdentityNum() != null)
                VALUES("identity_num", "#{identityNum}");
            if (userInfo.getBirthday() != null)
                VALUES("birthday", "#{birthday}");
            if (userInfo.getWorkYears() != null)
                VALUES("work_years", "#{workYears}");
            if (userInfo.getPoliticalStatus() != null)
                VALUES("political_status", "#{politicalStatus}");
            if (userInfo.getDeptId() != null)
                VALUES("dept_id", "#{deptId}");
            if (userInfo.getDeptName() != null)
                VALUES("dept_name", "#{deptName}");
            if (userInfo.getPosition() != null)
                VALUES("position", "#{position}");
            if (userInfo.getSchool() != null)
                VALUES("school", "#{school}");
            if (userInfo.getPosition() != null)
                VALUES("profession", "#{profession}");
            if (userInfo.getIndustryName() != null)
                VALUES("industry_name", "#{industryName}");
            if (userInfo.getExtensionField() != null)
                VALUES("extension_field", "#{extensionField}");
        }}.toString();
    }

    public String update(TUserInfo userInfo) {
        return new SQL() {{
            UPDATE("t_user_info");
            if (userInfo.getMobile() != null)
                SET("mobile = #{mobile}");
            if(userInfo.getPassword() != null)
                SET("password = #{password}");
            if (userInfo.getName() != null)
                SET("name = #{name}");
            if (userInfo.getEmail() != null)
                SET("email = #{email}");
            if (userInfo.getGender() != null)
                SET("gender = #{gender}");
            if (userInfo.getHighestEducation() != null)
                SET("highest_education = #{highestEducation}");
            if (userInfo.getIdentityNum() != null)
                SET("identity_num = #{identityNum}");
            if (userInfo.getIndustryName() != null)
                SET("industry_name = #{industryName}");
            if (userInfo.getBirthday() != null)
                SET("birthday = #{birthday}");
            if (userInfo.getWorkYears() != null)
                SET("work_years = #{workYears}");
            if (userInfo.getPoliticalStatus() != null)
                SET("political_status = #{politicalStatus}");
            if (userInfo.getDeptId() != null)
                SET("dept_id = #{deptId}");
            if (userInfo.getDeptName() != null)
                SET("dept_name = #{deptName}");
            if (userInfo.getPosition() != null)
                SET("position = #{position}");
            if (userInfo.getSchool() != null)
                SET("school = #{school}");
            if (userInfo.getProfession() != null)
                SET("profession = #{profession}");
            if (userInfo.getExtensionField() != null)
                SET("extension_field = #{extensionField}");
            WHERE("id = #{id}");
        }}.toString();
    }

    public String updateStatus(String account, Integer newStatus) {
        return new SQL() {{
            UPDATE("t_user_info");
            SET("status = {newStatus}");
            WHERE("account = #{account}");
        }}.toString();
    }

    public String getCntForUniqueField(String fieldName, String value, Integer companyId, Integer projectId, Integer taskId, String account) {
        StringBuffer sb = new StringBuffer();

        sb.append("select count(*) from t_user_info u ");
        sb.append("where u.company_id = #{companyId} ");
        sb.append("and u." + fieldName + " = #{value} ");
        sb.append("and u.account <> #{account}");
//        sb.append("and u.status not in (" + TUserInfoStatus. + ")");
        if (projectId != null && taskId != null) {
            sb.append("and u.id in (select user_id from t_user_exam where project_id = #{projectId} and task_id = #{taskId} and status not in (" + TUserExamStatus.DELETE.getValue() + ") )");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public String getUserByAccount(String account, Integer companyId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from t_user_info u ");
        sb.append("where u.company_id = #{companyId} ");
        sb.append("and account = #{account} ");
        return sb.toString();
    }

    public String getUserByMobile(String mobile) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from t_user_info u ");
        sb.append("where u.mobile = #{mobile} ");
        return sb.toString();
    }

    public String getUser(Integer id, Integer companyId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from t_user_info u ");
        sb.append("where u.company_id = #{companyId} ");
        sb.append("and id = #{id} ");
        return sb.toString();
    }

    public String updateUserPassword(DUpdatePassword data) {
        StringBuffer sb = new StringBuffer();
        sb.append("update t_user_info u");
        sb.append(" set u.password =#{data.newPassword}");
        sb.append(" where u.id = #{data.userId} ");
        return sb.toString();
    }

    public String builUpdatePasswordSql(DReSetPassword data, String password, List<Integer> idList) {
       /* return new SQL(){{
            UPDATE("t_user_info");
            SET("password = #{password}");
            WHERE("id " + SqlBuilderUtil.buildInSql("idList", idList.size()));
        }}.toString();*/
        StringBuilder sb = new StringBuilder();
        sb.append("update t_user_info a \n")
                .append(" inner join t_user_exam e on a.id = e.user_id \n")
                .append(" left join t_exercise t on t.user_id = e.user_id and t.task_id = e.task_id \n")
                .append(" set a.password =#{password}")
                .append(" where e.project_id =#{data.projectId} and e.task_id = #{data.taskId} \n")
                .append(" and e.company_id = #{data.companyId} \n")
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
            sb.append(" and a.account like  concat('%', #{data.key2}, '%') \n");
        } else if (data.isEmail()) {
            sb.append(" and a.email  like  concat('%', #{data.key2}, '%') \n");
        } else if (data.isName()) {
            sb.append(" and a.name  like  concat('%', #{data.key2}, '%') \n");
        } else if (data.isMobile()) {
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
        if (idList != null && idList.size() > 0) {
            sb.append(" and e.user_id " + SqlBuilderUtil.buildInSql("idList", idList.size()));
        }
        //答题状态
        if (!StringUtils.isEmpty(data.getStatus())){
            String[] status = data.getStatus().split(",");
            String statusStr = data.getStatus();
            //全选则不查询
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
        return sb.toString();
    }

    public String getMaxAccountByPreSubffix(Integer companyId, String pre, String suffix) {
        return new SQL() {{
            SELECT("substr(account, " + (pre.length() + 1) + ", length(account) - " + (pre.length() + suffix.length()) + ") - 0 ");
            FROM("t_user_info");
            WHERE("company_id = #{companyId}");
            WHERE("account like concat(#{pre}, '%') ");
            WHERE("account like concat('%', #{suffix})");
            ORDER_BY("substr(account, " + (pre.length() + 1) + ", length(account) - " + (pre.length() + suffix.length()) + ") - 0 desc");
        }}.toString() + " limit 1";
    }


    public String getReceiverStatistics(DReSetPassword data, List<Integer> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append("  select      \n")
                .append("  count(a.id) totalCount,count(a.mobile) hasSmsCount,count(a.email) hasEmailCount \n ")
                .append(" from t_user_info a \n")
                .append(" left join t_user_exam e on a.id = e.user_id  \n")
                .append(" left join t_exercise t on t.user_id = e.user_id and t.task_id = e.task_id \n")
                .append(" where e.project_id =#{data.projectId} and e.task_id = #{data.taskId} \n")
                .append(" and e.company_id = #{data.companyId} \n")
                .append(" and e.id is not null and e.status > " + TUserExamStatus.DELETE.getValue());
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
            sb.append(" and a.birthday <= #{data.key1End} ");
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
        if (idList != null && idList.size() > 0) {
            sb.append(" and e.user_id " + SqlBuilderUtil.buildInSql("idList", idList.size()));
        }
        //答题状态
        if (!StringUtils.isEmpty(data.getStatus())){
            String[] status = data.getStatus().split(",");
            String statusStr = data.getStatus();
            //全选则不查询
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
        return sb.toString();
    }

    public String getUserList(DReSetPassword data, List<Integer> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append("  select      \n")
                .append("  a.* \n ")
                .append(" from t_user_info a \n")
                .append(" left join t_user_exam e on a.id = e.user_id  \n")
                .append(" left join t_exercise t on t.user_id = e.user_id and t.task_id = e.task_id \n")
                .append(" where e.project_id =#{data.projectId} and e.task_id = #{data.taskId} \n")
                .append(" and e.company_id = #{data.companyId} \n")
                .append(" and e.id is not null and e.status > " + TUserExamStatus.DELETE.getValue());
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
            sb.append(" and  a.birthday<= #{data.key1End} ");
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
        if (idList != null && idList.size() > 0) {
            sb.append(" and e.user_id " + SqlBuilderUtil.buildInSql("idList", idList.size()));
        }
        //答题状态
        if (!StringUtils.isEmpty(data.getStatus())){
            String[] status = data.getStatus().split(",");
            String statusStr = data.getStatus();
            //全选则不查询
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
        //System.out.println(sb.toString());
        return sb.toString();
    }
}
