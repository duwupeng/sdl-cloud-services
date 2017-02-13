package com.talebase.cloud.ms.notify.dao;

import com.talebase.cloud.base.ms.notify.domain.TNotifyRecord;
import com.talebase.cloud.base.ms.notify.dto.DNotifyRecordStatus;
import com.talebase.cloud.base.ms.notify.dto.DPageSearchData;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyRecordSendStatus;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.PermissionUtil;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-28.
 */
public class NotifyRecordSqlProvider {

    public String getNotifyRecords(String orgCode, Integer companyId, String sender, PageRequest pageReq, DPageSearchData dPageSearchData, List<Integer> ids) {
        String param = "";
        if (dPageSearchData.getProjectId() != null) {
            param += " and project_id = #{dPageSearchData.projectId}";
        }
        if (dPageSearchData.getTaskId() != null) {
            param += " and task_id = #{dPageSearchData.taskId}";
        }
        if (!StringUtil.isEmpty(dPageSearchData.getSendDateBegin()) && !StringUtil.isEmpty(dPageSearchData.getSendDateEnd())) {
            param += " and send_date between #{dPageSearchData.sendDateBegin} and #{dPageSearchData.sendDateEnd} ";
        } else if (StringUtil.isEmpty(dPageSearchData.getSendDateBegin()) && !StringUtil.isEmpty(dPageSearchData.getSendDateEnd())) {
            param += " and send_date <= #{dPageSearchData.sendDateEnd} ";
        } else if (!StringUtil.isEmpty(dPageSearchData.getSendDateBegin()) && StringUtil.isEmpty(dPageSearchData.getSendDateEnd())) {
            param += " and send_date >= #{dPageSearchData.sendDateBegin} ";
        }
        if (dPageSearchData.isSearchByAccount()) {
            param += " and account like  concat('%', #{dPageSearchData.key}, '%')";
        } else if (dPageSearchData.isSearchByEmail()) {
            param += " and email like  concat('%', #{dPageSearchData.key}, '%')";
        } else if (dPageSearchData.isSearchByName()) {
            param += " and name like   concat('%', #{dPageSearchData.key}, '%')";
        } else if (dPageSearchData.isSearchByMobile()) {
            param += " and mobile like  concat('%', #{dPageSearchData.key}, '%')";
        }

        if (dPageSearchData.getSendStatus() != null) {
            param += " and send_status = #{dPageSearchData.sendStatus}";
        }
        if (dPageSearchData.getRoleId() != null) {
            param += " and role_id = #{dPageSearchData.roleId}";
        }
        if (ids != null && ids.size() > 0) {
            param += " and id " + SqlBuilderUtil.buildInSql("ids", ids.size());
        }

        String sql = "select id, company_id, send_subject,"
                + " send_content, send_type, role_id, project_id,"
                + " task_id, account, name, email,"
                + " mobile, sender, send_date, send_status"
                + " from t_notify_record "
                + " where company_id=#{companyId} "
                + " and send_type= #{dPageSearchData.sendType}"
                + param
                + PermissionUtil.projectExPermissionSqlIn("project_id", "sender", "sender", "companyId", "orgCode")
                + " limit #{pageReq.start},#{pageReq.limit}";
        return sql;
    }

    public String getCount(String orgCode, Integer companyId, String sender, DPageSearchData dPageSearchData) {
        String param = "";
        if (dPageSearchData.getProjectId() != null) {
            param += " and project_id = #{dPageSearchData.projectId}";
        }
        if (dPageSearchData.getTaskId() != null) {
            param += " and task_id = #{dPageSearchData.taskId}";
        }
        if (!StringUtil.isEmpty(dPageSearchData.getSendDateBegin()) && !StringUtil.isEmpty(dPageSearchData.getSendDateEnd())) {
            param += " and send_date between #{dPageSearchData.sendDateBegin} and #{dPageSearchData.sendDateEnd} ";
        } else if (StringUtil.isEmpty(dPageSearchData.getSendDateBegin()) && !StringUtil.isEmpty(dPageSearchData.getSendDateEnd())) {
            param += " and send_date <= #{dPageSearchData.sendDateEnd}";
        } else if (!StringUtil.isEmpty(dPageSearchData.getSendDateBegin()) && StringUtil.isEmpty(dPageSearchData.getSendDateEnd())) {
            param += " and send_date >= #{dPageSearchData.sendDateBegin}";
        }
        if (dPageSearchData.isSearchByAccount()) {
            param += " and account like  concat('%', #{dPageSearchData.key}, '%')";
        } else if (dPageSearchData.isSearchByEmail()) {
            param += " and email like  concat('%', #{dPageSearchData.key}, '%')";
        } else if (dPageSearchData.isSearchByName()) {
            param += " and name like   concat('%', #{dPageSearchData.key}, '%')";
        } else if (dPageSearchData.isSearchByMobile()) {
            param += " and mobile like  concat('%', #{dPageSearchData.key}, '%')";
        }

        if (dPageSearchData.getSendStatus() != null) {
            param += " and send_status = #{dPageSearchData.sendStatus}";
        }
        if (dPageSearchData.getRoleId() != null) {
            param += " and role_id = #{dPageSearchData.roleId}";
        }

        String sql = "select count(*) cnt from (select id, company_id, send_subject,"
                + " send_content, send_type, role_id, project_id,"
                + " task_id, account, name, email,"
                + " mobile, sender, send_date, send_status"
                + " from t_notify_record where"
                + " company_id=#{companyId} "
                + " and send_type= #{dPageSearchData.sendType}"
                + param
                + PermissionUtil.projectExPermissionSqlIn("project_id", "sender", "sender", "companyId", "orgCode")
                + " )a";
        return sql;
    }

    public String insert(TNotifyRecord tNotifyRecord) {
        return (new SQL() {
            {
                INSERT_INTO("t_notify_record");
                VALUES("company_id", "#{companyId}");
                VALUES("send_subject", "#{sendSubject}");
                VALUES("send_content", "#{sendContent}");
                VALUES("role_id", "#{roleId}");
                VALUES("project_id", "#{projectId}");
                VALUES("project_name", "#{projectName}");
                VALUES("task_id", "#{taskId}");
                VALUES("task_name", "#{taskName}");
                VALUES("account", "#{account}");
                VALUES("name", "#{name}");
                VALUES("email", "#{email}");
                VALUES("mobile", "#{mobile}");
                VALUES("send_type", "#{sendType}");
                VALUES("sender", "#{sender}");
                VALUES("send_date", "#{sendDate}");
                VALUES("send_count", "#{sendCount}");
            }
        }).toString();
    }

    public String update(TNotifyRecord tNotifyRecord) {
        return (new SQL() {
            {
                UPDATE("t_notify_record");
                SET("send_status = #{sendStatus}");
                WHERE("id = #{id}");
            }
        }).toString();
    }

    public String getExportList(String orgCode, Integer companyId, String sender, Integer sendType) {

        String sql = new SQL() {{
            SELECT("project_name,task_name,account,name,email,mobile,send_subject,send_content,sender,send_date,send_status");
            FROM("t_notify_record");
            WHERE("company_id = #{companyId}");
            WHERE("send_type = #{sendType}");
        }}.toString();
        sql = sql + PermissionUtil.projectExPermissionSqlIn("project_id", "sender", "sender", "companyId", "orgCode");
        return sql;
    }

    public String updateTimes(List<Integer> idList) {
        return new SQL() {{
            UPDATE("t_notify_record");
            SET("send_times = send_times + 1");
            SET("send_status = " + TNotifyRecordSendStatus.SENDING.getValue());
            WHERE("id " + SqlBuilderUtil.buildInSql("idList", idList.size()));
        }}.toString();
    }

    public String updateStatus(DNotifyRecordStatus dNotifyRecordStatus) {
        return new SQL() {{
            UPDATE("t_notify_record");
            SET("send_status = #{status}");
            SET("send_date = #{sendTime}");
            WHERE("id = #{id}");
        }}.toString();
    }

    public String querySendding() {
        return new SQL() {{
            SELECT("*");
            FROM("t_notify_record");
            WHERE("send_status = " + TNotifyRecordSendStatus.SENDING.getValue());
        }}.toString();
    }

    public String getById(List<Integer> idList) {
        return new SQL() {{
            SELECT("*");
            FROM("t_notify_record");
            WHERE("id " + SqlBuilderUtil.buildInSql("idList", idList.size()));
        }}.toString();
    }
}
