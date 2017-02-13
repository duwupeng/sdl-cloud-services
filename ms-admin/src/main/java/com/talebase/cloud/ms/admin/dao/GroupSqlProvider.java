package com.talebase.cloud.ms.admin.dao;

import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.base.ms.admin.enums.TGroupStatus;
import com.talebase.cloud.common.util.PermissionEnum;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.ms.admin.MsAdminApplication;
import org.apache.ibatis.annotations.Case;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.talebase.cloud.ms.admin.MsAdminApplication.OUTERADMIN;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
public class GroupSqlProvider {

    public String insert(TGroup group){
        return new SQL(){{
            INSERT_INTO("t_group");
            VALUES("parent_id", "#{parentId}");
            VALUES("name", "#{name}");
            VALUES("status", TGroupStatus.EFFECTIVE.getValue() + "");
            VALUES("creater", "#{creater}");
            VALUES("created_date", "now()");
            VALUES("modifier", "#{modifier}");
            VALUES("modified_date", "now()");
//            VALUES("org_code", "#{orgCode}");
            VALUES("company_id", "#{companyId}");
        }}.toString();
    }

    public String findGroupsByIds(Integer companyId, List<Integer> groupIds){
        return new SQL(){{
            SELECT("*");
            FROM("t_group");
            WHERE("company_id = #{companyId}");
            WHERE("id " + SqlBuilderUtil.buildInSql("groupIds", groupIds.size()));
        }}.toString();
    }

    public String findAdminCntByGroups(List<String> orgCodes){
        String sql = new SQL(){{
            SELECT("count(*)");
            FROM("t_group_admin ga");
            JOIN("t_group g on ga.group_id = g.id");
        }}.toString();

        sql += " where ( 0 = 1 ";
        for(int i = 0; i < orgCodes.size(); i++){
            sql += " or g.org_code like concat(#{orgCodes[" + i + "]}, '%')";
        }
        sql += ")";
        return sql;
    }

    public String updateStatusToDelete(List<String> orgCodes, Integer companyId, String operatorName){
        String sql = new SQL(){{
            UPDATE("t_group");
            SET("status = " + TGroupStatus.DELETED.getValue() + "");
            SET("modifier = #{operatorName}");
            SET("modified_date = now()");
            WHERE("company_id = #{companyId}");
        }}.toString();

        StringBuffer sb = new StringBuffer("and ( ");
        for(int i = 0; i < orgCodes.size(); i++){
            sb.append("org_code like concat('%', #{orgCodes[" + i + "]}, '%') or ");
        }
        sb.append(" 1 = 0)");

        return sql + sb.toString();
    }

    public String findGroupsWithAdminCnt(Integer companyId,List<String> permissions, String orgCode){
//        return "select g.id, g.name, g.org_code, \n" +
//                "case when count(ga.id) > 0 then count(ga.id) else 0 end as member_num \n" +
//                "from t_group g left join t_group_admin ga on g.id = ga.group_id \n" +
//                "where g.company_id = #{companyId} \n" +
//                "and g.status in (" + TGroupStatus.EFFECTIVE.getValue() + ") \n" +
//                "group by g.id, g.name, g.org_code \n" +
//                "order by g.org_code";

       /* String sql = new SQL(){{
            SELECT("g.id, g.name, g.org_code,case when count(ga.id) > 0 and  max(tr.id) is not null  then count(ga.id) else 0 end as member_num");
            FROM("t_group g");
            if(!permissions.contains(PermissionEnum.c2_1.name())){
                if(StringUtils.isEmpty(orgCode)){
                    LEFT_OUTER_JOIN("t_group_admin ga on g.id = ga.group_id and locate( g.org_code,'') > 0");
                }else{
                    LEFT_OUTER_JOIN(" t_group_admin ga on g.id = ga.group_id and locate( #{orgCode},g.org_code) > 0 ");
                }
            }else{
                LEFT_OUTER_JOIN("t_group_admin ga on g.id = ga.group_id");
            }
            LEFT_OUTER_JOIN(" t_role_admin tra on tra.admin_id = ga.admin_id ");
            LEFT_OUTER_JOIN(" t_role tr on tr.id = tra.role_id and tr.name <> '" + MsAdminApplication.OUTERADMIN + "'");
            WHERE(" g.company_id = #{companyId} and g.status in (" + TGroupStatus.EFFECTIVE.getValue() + ") ");
            GROUP_BY("g.id, g.name, g.org_code");
            ORDER_BY("g.org_code");
        }}.toString();*/

        StringBuffer sql = new StringBuffer();
        sql.append(" select gt.id,gt.name, gt.org_code,SUM(gt.member_num) member_num from ( \n")
           .append(" SELECT g.id, g.name, g.org_code,(case when count(ga.id) > 0 and tr.name<>'"+ MsAdminApplication.OUTERADMIN+"'  then count(ga.id) else 0 end) as member_num  \n")
           .append(" FROM t_group g \n");
            if(!permissions.contains(PermissionEnum.c2_1.name())){
                if(StringUtils.isEmpty(orgCode)){
                    sql.append(" LEFT  JOIN  t_group_admin ga on g.id = ga.group_id and locate( g.org_code,'') > 0");
                }else{
                    sql.append(" LEFT  JOIN  t_group_admin ga on g.id = ga.group_id and locate( #{orgCode},g.org_code) > 0 ");
                }
            }else{
                sql.append(" LEFT  JOIN  t_group_admin ga on g.id = ga.group_id ");
            }
           sql.append(" LEFT  JOIN  t_role_admin tra on tra.admin_id = ga.admin_id  \n")
            .append(" LEFT  JOIN  t_role tr on tr.id = tra.role_id  \n")
           .append(" WHERE ( g.company_id = #{companyId} and g.status > "+TGroupStatus.DELETED.getValue()+" ) \n")
           .append(" GROUP BY g.id, g.name, g.org_code ,tr.name \n")
           .append("  )gt \n")
           .append(" GROUP BY gt.id, gt.name, gt.org_code \n")
           .append("  ORDER BY gt.org_code ");
        return sql.toString();
    }



    public String findGroupsByOrgCode(Integer companyId, String orgCode){
        return new SQL(){{
            SELECT("*");
            FROM("t_group g");
            WHERE("g.company_id = #{companyId} ");
//            if(StringUtils.isEmpty(orgCode)){
//                WHERE(" locate( g.org_code,'') > 0 ");
//            }else{
//                WHERE(" locate( #{orgCode},g.org_code) > 0 ");
//            }
            WHERE(" g.status>" + TGroupStatus.DELETED.getValue() + " ");
        }}.toString();
    }

    public String get(Integer groupId){
        String sql = new SQL(){{
            SELECT("*");
            FROM("t_group g");
            WHERE("id = #{groupId} ");
            WHERE(statusDeleted());
        }}.toString();

        return sql;
    }

    public String getCntByName(Integer companyId, String name){
        return "select count(*) from t_group where company_id = #{companyId} and name = #{name} and " + statusDeleted();
    }

    private String statusDeleted(){
        return "status not in (" + TGroupStatus.DELETED.getValue() + ") ";
    }

    public String updateNameSameId(Integer companyId, String name,Integer groupId){
        return "select count(*) from t_group where company_id = #{companyId} and name = #{name} and id = #{groupId}";
    }

}
