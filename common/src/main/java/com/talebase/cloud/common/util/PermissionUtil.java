package com.talebase.cloud.common.util;

/**
 * Created by kanghong.zhao on 2016-12-2.
 */
public class PermissionUtil {

//    public static String getDataPermissionSqlJoin(String pre, String companyIdParam, String orgCodeParam){
//        StringBuffer sb = new StringBuffer(" join t_group_admin t_group_admin on ");
//        sb.append(pre + " = t_group_admin.account ");
//        sb.append("and t_group_admin.company_id = #{" + companyIdParam + "} ");
//        sb.append("and t_group_admin.org_code like concat('%', #{" + orgCodeParam + "}, '%') ");
//        return sb.toString();
//    }
//
//    public static String getDataPermissionSqlJoin(String pre){
//        return getDataPermissionSqlJoin(pre, "companyId", "orgCode");
//    }

    public static String getDataPermissionSqlIn(String pre, String companyIdParam, String orgCodeParam){
        StringBuffer sb = new StringBuffer(pre + " in ( ");
        sb.append("select account from t_group_admin t_group_admin ");
        sb.append("where t_group_admin.company_id = #{" + companyIdParam + "} ");
        sb.append("and t_group_admin.org_code like concat('%', #{" + orgCodeParam + "}, '%') ");
        sb.append(")");
        return sb.toString();
    }

    public static String getDataPermissionSqlIn(String pre){
        return getDataPermissionSqlIn(pre, "companyId", "orgCode");
    }

    private static String projectExPermissionSql(String pre, String accountParam){
        StringBuffer sb = new StringBuffer(" " + pre + " in ( ");
        sb.append("select project_id from t_project_admin t_project_admin ");
        sb.append("where t_project_admin.account = #{" + accountParam + "} ");
        sb.append(")");
        return sb.toString();
    }

    public static String projectExPermissionSqlIn(String projectId, String creater, String accountParam, String companyIdParam, String orgCodeParam){
        StringBuffer sb = new StringBuffer(" and ( ");
        sb.append(getDataPermissionSqlIn(creater, companyIdParam, orgCodeParam));
        sb.append(" or ");
        sb.append(projectExPermissionSql(projectId, accountParam));
        sb.append(" ) ");
        return sb.toString();
    }

    /**
     * and (
     p.creater in
     ( select account from t_group_admin t_group_admin
     where t_group_admin.company_id = 1
     and t_group_admin.org_code like concat('%', '', '%')
     )
     or
     p.id in
     ( select project_id from t_project_admin t_project_admin where t_project_admin.account = '' )
     )
     * @return
     */
    public static String projectExPermissionSqlIn(String projectId, String creater){
        return projectExPermissionSqlIn(projectId, creater, "account", "companyId", "orgCode");
    }

//    //"and t.company_id = #{companyId} and t.org_code like ('%', #{orgCode}, '%')"
//    public static String getDataPermissionStr(String pre){
//        return getDataPermissionStr(pre + ".", "companyId", "orgCode");
//    }
//
//    //"and company_id = #{companyId} and org_code like ('%', #{orgCode}, '%')"
//    public static String getDataPermissionStr(){
//        return getDataPermissionStr("", "companyId", "orgCode");
//    }
//
//    "and company_id = #{companyId} and org_code like ('%', #{orgCode}, '%')"
}
