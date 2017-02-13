package com.talebase.cloud.ms.admin.dao;

import com.talebase.cloud.base.ms.admin.domain.TRole;
import com.talebase.cloud.base.ms.admin.enums.TRoleStatus;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.util.PermissionEnum;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.ms.admin.MsAdminApplication;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.talebase.cloud.ms.admin.MsAdminApplication.OUTERADMIN;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
public class RoleSqlProvider {

    public String insert(TRole role){
        return new SQL(){{
            INSERT_INTO("t_role");
            VALUES("name", "#{name}");
            VALUES("status", TRoleStatus.EFFECTIVE.getValue() + "");
            VALUES("creater", "#{creater}");
            VALUES("created_date", "now()");
            VALUES("modifier", "#{modifier}");
            VALUES("modified_date", "now()");
            VALUES("company_id", "#{companyId}");
            VALUES("role_type", "#{roleType}");
        }}.toString();
    }

    public String findRolesByIds(Integer companyId, List<Integer> roleIds){
        return new SQL(){{
            SELECT("*");
            FROM("t_role");
            WHERE("company_id = #{companyId}");
            WHERE("id " + SqlBuilderUtil.buildInSql("roleIds", roleIds.size()));
        }}.toString();
    }

    public String findAdminCntByRoles(List<Integer> roleIds){
        return new SQL(){{
            SELECT("count(*)");
            FROM("t_role_admin ra");
            JOIN("t_role r on ra.role_id = r.id");
            WHERE("r.id " + SqlBuilderUtil.buildInSql("roleIds", roleIds.size()));
        }}.toString();
    }

    public String updateStatusToDelete(List<Integer> roleIds, Integer companyId, String operatorName){
        String sql = new SQL(){{
            UPDATE("t_role");
            SET("status = " + TRoleStatus.DELETED.getValue() + "");
            SET("modifier = #{operatorName}");
            SET("modified_date = now()");
            WHERE("company_id = #{companyId}");
            WHERE("id" + SqlBuilderUtil.buildInSql("roleIds", roleIds.size()));
        }}.toString();

        return sql;
    }

//    public String getCntByName(){
//
//        return new SQL(){{
//            SELECT("count(*)");
//            FROM("t_role");
//            WHERE("company_id = #{companyId}");
//            WHERE("");
//        }}.toString();
//
//
//        return "select count(*) from t_role where company_id = #{companyId} and name = #{name}";
//    }

    public String findRolesWithAdminCnt(Integer companyId,List<String> permissions, String orgCode){
        String sql = new SQL(){{
            SELECT("r.id, r.name,case when count(ra.id) > 0 and max(a.id )is not null then count(ra.id) else 0 end as member_num,r.role_type");
            FROM("t_role r");
            LEFT_OUTER_JOIN("t_role_admin ra on r.id = ra.role_id ");
            if(!permissions.contains(PermissionEnum.c2_1.name())){
                if(StringUtils.isEmpty(orgCode)){
                    LEFT_OUTER_JOIN("t_admin a on a.id = ra.admin_id and locate( a.org_code,'') > 0");
                }else{
                    LEFT_OUTER_JOIN(" t_admin a on a.id = ra.admin_id and locate( #{orgCode},a.org_code) > 0 ");
                }
            }else{
                LEFT_OUTER_JOIN("t_admin a on a.id = ra.admin_id ");
            }
            WHERE(" r.company_id = #{companyId} and r.status in (" + TRoleStatus.EFFECTIVE.getValue() + ") and r.name <> '" + OUTERADMIN + "'");
            GROUP_BY("r.id, r.name");
            ORDER_BY("r.id");
        }}.toString();

        return sql;

    }

    public String findRolesWithAdminCntNum(Integer companyId,List<String> permissions, String orgCode){

        String sql = new SQL(){{
            SELECT("count(distinct tr.id)");
            FROM("t_role tr");
            LEFT_OUTER_JOIN(" t_role_admin tra on tra.role_id = tr.id ");
            LEFT_OUTER_JOIN(" t_admin ta on ta.id = tra.admin_id ");
//            if(!permissions.contains(PermissionEnum.c2_1.name())){
//                if(StringUtils.isEmpty(orgCode)){
//                WHERE(" locate( ta.org_code,'') > 0 ");
//                }else{
//                WHERE(" locate( #{orgCode},ta.org_code) > 0 ");
//                }
//            }
            WHERE(" tr.company_id = #{companyId} and tr.status in (" + TRoleStatus.EFFECTIVE.getValue() + ")  and tr.name <> '" + OUTERADMIN + "'");

        }}.toString();
//        System.out.println(sql);
        return sql;
    }

    public String findRolesWithAdminCntByPage(Integer companyId, PageRequest pageReq,List<String> permissions, String orgCode){
//        String sql = new SQL(){{
//            SELECT("r.id, r.name,case when count(ra.id) > 0 and max(a.id ) is not null then count(ra.id) else 0 end as member_num");
//            FROM("t_role r");
//            LEFT_OUTER_JOIN("t_role_admin ra on r.id = ra.role_id ");
//            if(!permissions.contains(PermissionEnum.c2_1.name())){
//                if(StringUtils.isEmpty(orgCode)){
//                    LEFT_OUTER_JOIN("t_admin a on a.id = ra.admin_id and locate( a.org_code,'') > 0");
//                }else{
//                    LEFT_OUTER_JOIN(" t_admin a on a.id = ra.admin_id and locate( #{orgCode},a.org_code) > 0 ");
//                }
//            }else{
//                LEFT_OUTER_JOIN("t_admin a on a.id = ra.admin_id");
//            }
//            WHERE(" r.company_id = #{companyId} and r.status in (" + TRoleStatus.EFFECTIVE.getValue() + ") and r.name <> '" + OUTERADMIN + "'");
//            GROUP_BY("r.id, r.name");
//            ORDER_BY("r.id ");
//        }}.toString();
//        return sql;
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.id,t.name,SUM(t.member_num)  member_num,t.role_type from ( \n")
                .append(" SELECT r.id, r.name,  \n")
                .append(" (select  count(1) from t_admin a where a.id = ra.admin_id and company_id =#{companyId} and a.status > "+TRoleStatus.DELETED.getValue()+"  \n ");
                if(!permissions.contains(PermissionEnum.c2_1.name())){
                    if(StringUtils.isEmpty(orgCode)){
                        sql.append(" and locate( a.org_code,'') > 0");
                    }else{
                        sql.append("  and locate( #{orgCode},a.org_code) > 0 ");
                    }
                }
               sql.append(" )  as member_num,r.role_type ")
                  .append(" FROM t_role r \n")
                  .append(" LEFT join t_role_admin ra on r.id = ra.role_id  \n")
                  .append(" where r.company_id =#{companyId} and r.status > "+TRoleStatus.DELETED.getValue()+"  and r.name <> '" + OUTERADMIN + "'\n")
                  .append(" )t group by t.id, t.name,t.role_type order by t.id  "+SqlBuilderUtil.buildPageLimit(pageReq)+"");

        return sql.toString();

//        return "select r.id, r.name, \n" +
//                "case when count(ra.id) > 0 then count(ra.id) else 0 end as member_num \n" +
//                "from t_role r left join t_role_admin ra on r.id = ra.role_id \n" +
//                "where r.company_id = #{companyId} \n" +
//                "and r.status in (" + TRoleStatus.EFFECTIVE.getValue() + ")\n" +
//                "and r.name <> '"  + OUTERADMIN +"' \n" +
//                "group by r.id, r.name \n" +
//                "order by r.id " + SqlBuilderUtil.buildPageLimit(pageReq);
    }

    public String findCompanyRoles(Integer companyId) {
        return new SQL() {{
            SELECT("*");
            FROM("t_role ");
            WHERE("company_id = #{companyId}");
            WHERE("name <> '" + OUTERADMIN + "' and status> " + TRoleStatus.DELETED.getValue() + "");
        }}.toString();
    }
    public String findALLCompanyRoles(Integer companyId) {
        return new SQL() {{
            SELECT("*");
            FROM("t_role ");
            WHERE("company_id = #{companyId}");
            WHERE(" status = " + TRoleStatus.EFFECTIVE.getValue() + "");
        }}.toString();
    }
    public String updateRoleNameSameId(Integer companyId, String name,Integer roleId){
        return "select count(*) from t_role where company_id = #{companyId} and name = #{name} and id = #{roleId}";
    }

}
