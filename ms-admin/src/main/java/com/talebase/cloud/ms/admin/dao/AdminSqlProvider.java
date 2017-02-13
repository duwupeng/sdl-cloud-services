package com.talebase.cloud.ms.admin.dao;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.domain.TGroupAdmin;
import com.talebase.cloud.base.ms.admin.domain.TRoleAdmin;
import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.admin.dto.DPageSearchData;
import com.talebase.cloud.base.ms.admin.enums.TAdminStatus;
import com.talebase.cloud.common.util.PermissionEnum;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.ms.admin.MsAdminApplication;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.talebase.cloud.ms.admin.MsAdminApplication.EXAMINER;


/**
 * Created by kanghong.zhao on 2016-11-7.
 */
public class  AdminSqlProvider {

    public String buildExportAdminsSql(Integer companyId,String orgCode, List<String> permissions){
        return new SQL(){{
            SELECT(" a.account,a.password,a.name,a.email," +
                    " tr.name as roleName,tg.name as groupName,a.status, " +
                    " a.mobile,a.creater,a.created_date ");
            FROM(" t_admin a");
            LEFT_OUTER_JOIN(" t_group_admin tga on tga.admin_id = a.id ");
            LEFT_OUTER_JOIN(" t_group tg on tg.id = tga.group_id ");
            LEFT_OUTER_JOIN(" t_role_admin tra on tra.admin_id = a.id ");
            LEFT_OUTER_JOIN(" t_role tr on tr.id = tra.role_id ");
            WHERE(" a.company_id =#{companyId} ");
            WHERE(" a.status > "+TAdminStatus.DELETED.getValue());
            WHERE(" tr.name <> '" +MsAdminApplication.OUTERADMIN+"'");
            if(!permissions.contains(PermissionEnum.c2_1.name())){
                if(StringUtils.isEmpty(orgCode)){
                    WHERE(" locate( a.org_code,'') > 0 ");
                }else{
                    WHERE(" locate( #{orgCode},a.org_code) > 0 ");
                }
            }
            ORDER_BY(" a.created_date desc ");
        }}.toString();
    }

    public String buildSelectSubordinateSql(Integer companyId,String orgCode){
        return new SQL(){{
            SELECT(" a.name,a.account" );
            FROM(" t_admin a");
            WHERE(" a.org_code like concat(#{orgCode}, '_','%') ");
            WHERE(" a.company_id =#{companyId} ");
            WHERE(" a.status in (" + TAdminStatus.EFFECTIVE.getValue() + ")");
        }}.toString();
    }

//        public String buildSelectExaminersSql(Integer companyId){
//        return new SQL(){{
//            SELECT(" ra.admin_id" );
//            FROM(" t_role_admin ra");
//            WHERE(" ra.role_id  in  (" SELECT(" r.id"), FROM(" t_role r"), WHERE(" r.name='考官' and company_id = #{companyId} ") ") ");
//
//            SELECT(" r.id" );
//            FROM(" t_role r");
//            WHERE(" r.name='考官' and company_id = #{companyId} ");
//            WHERE(" a.status = 1 ");
//
//        }}.toString();
//    }
    public String buildSelectExaminersSql(Integer companyId){
        return new SQL(){{
            SELECT("  a.name,a.account ");
            FROM(" t_admin a");
            INNER_JOIN(" t_role_admin ra on ra.admin_id = a.id ");
            INNER_JOIN(" t_role r on  r.id = ra.role_id and r.name='" + EXAMINER + "' and a.company_id = #{companyId} ");
            WHERE(" a.status = 1 ");

        }}.toString();
    }

    public String buildSelectAdminSql(Integer id){
        String sql = new SQL(){{
            SELECT(" a.*," +
                    "tr.id as roleId,tg.id as groupId");
            FROM(" t_admin a");
            LEFT_OUTER_JOIN(" t_group_admin tga on tga.admin_id = a.id ");
            LEFT_OUTER_JOIN(" t_group tg on tg.id = tga.group_id ");
            LEFT_OUTER_JOIN(" t_role_admin tra on tra.admin_id = a.id ");
            LEFT_OUTER_JOIN(" t_role tr on tr.id = tra.role_id ");
            WHERE(" a.id = #{id} ");

        }}.toString();
//        System.out.println(sql);
        return sql;
    }

    public String buildInsertSql(TAdmin user){
        return new SQL(){{
            INSERT_INTO("t_admin");
            VALUES("account", "#{account}");
            VALUES("password", "#{password}");
            VALUES("name",  "#{name}");
            VALUES("email",  "#{email}");
            VALUES("status",  "#{status}");
            VALUES("created_date",  "#{createdDate}");
            VALUES("creater",  "#{creater}");
            VALUES("org_code",  "#{orgCode}");
            VALUES("company_id",  "#{companyId}");
            VALUES("mobile",  "#{mobile}");
        }}.toString();
    }

    public String buildInsertGroupAdminSql(TGroupAdmin groupAdmin){
        return new SQL(){{
            INSERT_INTO("t_group_admin");
            VALUES("admin_id", "#{adminId}");
            VALUES("group_id", "#{groupId}");
        }}.toString();
    }

    public String buildInsertRoleAdminSql(TRoleAdmin tRoleAdmin){
        return new SQL(){{
            INSERT_INTO("t_role_admin");
            VALUES("admin_id", "#{adminId}");
            VALUES("role_id", "#{roleId}");
        }}.toString();
    }

    public String buildUpdateSql(TAdmin user){
        return new SQL(){{
            UPDATE("t_admin");
            if (!StringUtils.isEmpty(user.getAccount())){
                SET("account = #{account}");
            }
            if (!StringUtils.isEmpty(user.getPassword())){
                SET("password = #{password}");
            }
            if (!StringUtils.isEmpty(user.getName())){
                SET("name = #{name}");
            }
            if (!StringUtils.isEmpty(user.getEmail())){
                SET("email = #{email}");
            }
            if (!StringUtils.isEmpty(user.getStatus())){
                SET("status = #{status}");
            }
            if (!StringUtils.isEmpty(user.getModifiedDate())){
                SET("modified_date = #{modifiedDate}");
            }
            if (!StringUtils.isEmpty(user.getModifier())){
                SET("modifier = #{modifier}");
            }
            if (!StringUtils.isEmpty(user.getOrgCode())){
                SET("org_code = #{orgCode}");
            }
            if (!StringUtils.isEmpty(user.getMobile())){
                SET("mobile = #{mobile}");
            }
            WHERE("id = #{id}");
        }}.toString();
    }

    public String buildSelectSql(DAdmin admin){
        return new SQL(){{
            SELECT("account");
            FROM("t_admin");
            WHERE("account = #{account}");
            AND();
            WHERE("company_id = #{companyId}");
        }}.toString();
    }

    public String buildDelGroupAdminSql(TAdmin admin){
        return new SQL(){{
            DELETE_FROM("t_group_admin");
            WHERE("admin_id = #{id}");
        }}.toString();
    }

    public String buildDelRoleAdminSql(TAdmin admin){
        return new SQL(){{
            DELETE_FROM("t_role_admin");
            WHERE("admin_id = #{id}");
        }}.toString();
    }

    public String updateRoleAdminByAdminIdSql(TRoleAdmin tRoleAdmin){
        return new SQL(){{
            UPDATE("t_role_admin");
            if (!StringUtils.isEmpty(tRoleAdmin.getRoleId())){
                SET("role_id = #{roleId}");
            }
            WHERE("admin_id = #{adminId}");
        }}.toString();
    }

    public String updateGroupAdminByAdminIdSql(TGroupAdmin tGroupAdmin){
        return new SQL(){{
            UPDATE("t_group_admin");
            if (!StringUtils.isEmpty(tGroupAdmin.getGroupId())){
                SET("group_id = #{groupId}");
            }

            WHERE("admin_id = #{adminId}");
        }}.toString();
    }

    public String buildCountAdminSql(DPageSearchData data,String orgCode,Integer companyId, List<String> permissions){
        String sql = new SQL(){{
            SELECT( " count(1) total");
            FROM(" t_admin a");
            LEFT_OUTER_JOIN(" t_group_admin tga on tga.admin_id = a.id ");
            LEFT_OUTER_JOIN(" t_group tg on tg.id = tga.group_id ");
            LEFT_OUTER_JOIN(" t_role_admin tra on tra.admin_id = a.id ");
            LEFT_OUTER_JOIN(" t_role tr on tr.id = tra.role_id ");
            if(!permissions.contains(PermissionEnum.c2_1.name())){
                if(StringUtils.isEmpty(orgCode)){
                    WHERE(" locate( a.org_code,'') > 0 ");
                }else{
                    WHERE(" locate( #{orgCode},a.org_code) > 0 ");
                }
            }
            if (data.isSearchByAccount()){
                AND();
                WHERE(" a.account like  concat('%', #{data.key}, '%') ");
            }else if (data.isSearchByEmail()){
                AND();
                WHERE("  a.email like concat('%', #{data.key}, '%')  ");
            }else if (data.isSearchByName()){
                AND();
                WHERE(" a.name like  concat('%', #{data.key}, '%')  ");
            }else if (data.isSearchByMoblie()){
                AND();
                WHERE(" a.mobile like  concat('%', #{data.key}, '%')  ");
            }

            if (!StringUtils.isEmpty(data.getGroupId())
                    && data.getGroupId() > 0){
                AND();
                WHERE(" tg.id = #{data.groupId} ");
            }
            if ( !StringUtils.isEmpty(data.getRoleId()) &&
                    data.getRoleId() >0){
                AND();
                WHERE("  tr.id = #{data.roleId} ");
            }
            if (!StringUtils.isEmpty(data.getStatus())){
                AND();
                WHERE("  a.status = #{data.status} ");
            }
            WHERE(" a.company_id = #{companyId} ");
            WHERE(" a.status > "+TAdminStatus.DELETED.getValue());
            WHERE(" tr.name <> '" +MsAdminApplication.OUTERADMIN+"'");
        }}.toString();
        System.out.println(sql);
        return sql;
    }

    public String buildSearchAdminPageSql(DPageSearchData data,String orgCode,Integer index,Integer size,Integer companyId, List<String> permissions){
        String sql = new SQL(){{
            SELECT( " a.id,a.account,a.name,a.email,tr.name as roleName, " +
                    "tg.name as groupName,a.creater,a.created_date as createDate,a.status," +
                    " tg.id as groupId,tr.id as roleId,a.mobile ");
            FROM(" t_admin a");
            LEFT_OUTER_JOIN(" t_group_admin tga on tga.admin_id = a.id ");
            LEFT_OUTER_JOIN(" t_group tg on tg.id = tga.group_id ");
            LEFT_OUTER_JOIN(" t_role_admin tra on tra.admin_id = a.id ");
            LEFT_OUTER_JOIN(" t_role tr on tr.id = tra.role_id ");
            if(!permissions.contains(PermissionEnum.c2_1.name())){
                if(StringUtils.isEmpty(orgCode)){
                    WHERE(" locate( a.org_code,'') > 0 ");
                }else{
                    WHERE(" locate( #{orgCode},a.org_code) > 0 ");
                }
            }
            if (data.isSearchByAccount()){
                AND();
                WHERE(" a.account like  concat('%', #{data.key}, '%')  ");
            }else if (data.isSearchByEmail()){
                AND();
                WHERE("  a.email like  concat('%', #{data.key}, '%')   ");
            }else if (data.isSearchByName()){
                AND();
                WHERE(" a.name like   concat('%', #{data.key}, '%')   ");
            }else if (data.isSearchByMoblie()){
                AND();
                WHERE(" a.mobile like  concat('%', #{data.key}, '%')  ");
            }

            if (!StringUtils.isEmpty(data.getGroupId())
                    && data.getGroupId() > 0){
                AND();
                WHERE(" tg.id = #{data.groupId} ");
            }
            if ( !StringUtils.isEmpty(data.getRoleId()) &&
                    data.getRoleId() >0){
                AND();
                WHERE("  tr.id = #{data.roleId} ");
            }
            if (!StringUtils.isEmpty(data.getStatus())){
                AND();
                WHERE("  a.status = #{data.status} ");
            }
            WHERE(" a.company_id = #{companyId} ");
            WHERE(" a.status > "+TAdminStatus.DELETED.getValue());
            WHERE(" tr.name<>'"+ MsAdminApplication.OUTERADMIN+"'");
            ORDER_BY(" a.created_date desc limit #{index},#{size} ");

        }}.toString();
        return sql;
    }

    public String getAdminsByAccounts(Integer companyId, List<String> accounts){
        return new SQL(){{
            SELECT("*");
            FROM("t_admin");
            WHERE("account " + SqlBuilderUtil.buildInSql("accounts", accounts.size()));
            WHERE("company_id = #{companyId}");
//            WHERE("status in ( " + TAdminStatus.EFFECTIVE.getValue() + " )");
        }}.toString();
    }

}
