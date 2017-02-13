package com.talebase.cloud.ms.notify.dao;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateStatus;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateType;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateWhetherDefault;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.PermissionUtil;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-24.
 */
public class NotifyTemplateSqlProvider {

    public String query(ServiceHeader serviceHeader, PageRequest pageReq, Integer method) {

        String sql = "select * from (select id,company_id, name, content, subject, sign, method,type,"
                + " status, whether_default, creator, created_date, modifier, modified_date,"
                + " case when type = " + TNotifyTemplateType.CUSTOMIZE.getValue()
                + " and creator = #{serviceHeader.operatorName} then 1"
                + " when type = " + TNotifyTemplateType.SYSTEM.getValue()
                + " then 2 else 3 end sort from t_notify_template"
                + " where status in (" + TNotifyTemplateStatus.ENABLE.getValue() + "," + TNotifyTemplateStatus.DISABLE.getValue() + ")"
                + " and method = #{method}"+
                "   and company_id = #{serviceHeader.companyId}" +
                " and (" +
                PermissionUtil.getDataPermissionSqlIn(" creator", "serviceHeader.companyId", "serviceHeader.orgCode")
                + " or type = " + TNotifyTemplateType.SYSTEM.getValue() + "))a"
                + " order by sort asc,whether_default desc,created_date desc"
                + " limit #{pageReq.start},#{pageReq.limit}";
        return sql;
    }

    public String queryCount(ServiceHeader serviceHeader, Integer method) {
        String sql = "select count(*) from (select id,company_id, name, content, subject, sign, method,"
                + " status, whether_default, creator, created_date, modifier, modified_date,"
                + " case when type = " + TNotifyTemplateType.CUSTOMIZE.getValue()
                + " and creator = #{serviceHeader.operatorName} then 1"
                + " when type = " + TNotifyTemplateType.SYSTEM.getValue()
                + " then 2 else 3 end sort from t_notify_template"
                + " where status in (" + TNotifyTemplateStatus.ENABLE.getValue() + "," + TNotifyTemplateStatus.DISABLE.getValue() + ")"
                + " and method = #{method}" +
                "   and company_id = #{serviceHeader.companyId}" +
                " and (" +
                PermissionUtil.getDataPermissionSqlIn(" creator", "serviceHeader.companyId", "serviceHeader.orgCode")
                + " or type = " + TNotifyTemplateType.SYSTEM.getValue() + "))a"
                + " order by sort asc,whether_default desc,created_date desc";
        return sql;
    }

    public String queryTemplates(Integer companyId, String creator, Integer method) {
        String sql = "select id,company_id, name, content, subject, sign, whether_default, creator,created_date,"
                + " case when type = " + TNotifyTemplateType.CUSTOMIZE.getValue()
                + " and creator = #{creator}"
                + " and whether_default = " + TNotifyTemplateWhetherDefault.YES.getValue()
                + " then 1"
                + " when type = " + TNotifyTemplateType.SYSTEM.getValue()
                + " and whether_default = " + TNotifyTemplateWhetherDefault.YES.getValue()
                + " then 2 else 3 end sort from t_notify_template"
                + " where status = " + TNotifyTemplateStatus.ENABLE.getValue()
                + " and method = #{method}" +
                "   and company_id = #{companyId}" +
                "   and ((type = " + TNotifyTemplateType.CUSTOMIZE.getValue() + " and creator = #{creator}) or type = " + TNotifyTemplateType.SYSTEM.getValue() + ")"
                + " order by sort asc,whether_default desc,created_date desc";
        return sql;
    }

    public String querySystemTemplates(Integer companyId, String creator) {
        String sql = "select *"
                + " from t_notify_template"
                + " where status = " + TNotifyTemplateStatus.ENABLE.getValue()
                + " and company_id = #{companyId} " +
                "  and creator = #{creator} and type = " + TNotifyTemplateType.SYSTEM.getValue();
        return sql;
    }

    public String queryTemplateByName() {
        return (new SQL() {
            {
                SELECT("count(*)");
                FROM("t_notify_template");
                WHERE("company_id = #{companyId}");
                WHERE("method = #{method}");
                WHERE("id != #{id}");
                WHERE("name = #{name}");
                WHERE("status in (" + TNotifyTemplateStatus.ENABLE.getValue() + "," + TNotifyTemplateStatus.DISABLE.getValue() + ")");
            }
        }).toString();
    }

    public String insert(TNotifyTemplate tNotifyTemplate) {
        return (new SQL() {
            {
                INSERT_INTO("t_notify_template");
                VALUES("company_id", "#{companyId}");
                VALUES("name", "#{name}");
                VALUES("content", "#{content}");
                VALUES("subject", "#{subject}");
                VALUES("sign", "#{sign}");
                if(tNotifyTemplate.getType() != null){
                    VALUES("type", "#{type}");
                }
                VALUES("method", "#{method}");
                VALUES("whether_default", "#{whetherDefault}");
                VALUES("creator", "#{creator}");
            }
        }).toString();
    }

    public String updateEmail(TNotifyTemplate tNotifyTemplate) {
        return (new SQL() {
            {
                UPDATE("t_notify_template");
                SET("name = #{name}");
                SET("content = #{content}");
                SET("subject = #{subject}");
                SET("sign = #{sign}");
                SET("whether_default = #{whetherDefault}");
                SET("modifier = #{modifier}");
                SET("modified_date = now()");
                WHERE("id = #{id}");
            }
        }).toString();
    }

    public String updateSms(TNotifyTemplate tNotifyTemplate) {
        return (new SQL() {
            {
                UPDATE("t_notify_template");
                SET("name = #{name}");
                SET("content = #{content}");
                SET("whether_default = #{whetherDefault}");
                SET("modifier = #{modifier}");
                SET("modified_date = now()");
                WHERE("id = #{id}");
            }
        }).toString();
    }

    public String updateStatus() {
        return (new SQL() {
            {
                UPDATE("t_notify_template");
                SET("status = #{status}");
                SET("modifier = #{modifier}");
                SET("modified_date = now()");
                WHERE("id = #{id}");
            }
        }).toString();
    }

    public String updateDefault(Integer companyId, Integer id, Integer method) {
        return (new SQL() {
            {
                UPDATE("t_notify_template");
                SET("whether_default = " + TNotifyTemplateWhetherDefault.NO.getValue());
                WHERE("company_id = #{companyId}");
                WHERE("id != #{id}");
                WHERE("method = #{method}");
                WHERE("type = " + TNotifyTemplateType.CUSTOMIZE.getValue());
            }
        }).toString();
    }

    public String delete(String operatorName, List<Integer> id) {
        return (new SQL() {
            {
                UPDATE("t_notify_template");
                SET("status = " + TNotifyTemplateStatus.DELETE.getValue());
                SET("modifier = #{operatorName}");
                SET("modified_date = now()");
                WHERE("id " + SqlBuilderUtil.buildInSql("id", id.size()));
            }
        }).toString();
    }
}