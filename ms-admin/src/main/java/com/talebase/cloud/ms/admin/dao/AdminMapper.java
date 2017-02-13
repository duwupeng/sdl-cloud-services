package com.talebase.cloud.ms.admin.dao;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.domain.TGroupAdmin;
import com.talebase.cloud.base.ms.admin.domain.TRoleAdmin;
import com.talebase.cloud.base.ms.admin.dto.*;
import com.talebase.cloud.common.protocal.PageResponse;
import org.apache.ibatis.annotations.*;


import java.util.List;

/**
 * Created by kanghong.zhao on 2016-11-10.
 */
@Mapper
public interface AdminMapper {

    @InsertProvider(type = AdminSqlProvider.class, method = "buildInsertSql")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TAdmin admin);

    @InsertProvider(type = AdminSqlProvider.class, method = "buildInsertGroupAdminSql")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertGroupAdmin(TGroupAdmin tGroupAdmin);

    @InsertProvider(type = AdminSqlProvider.class, method = "buildInsertRoleAdminSql")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertRoleAdmin(TRoleAdmin tRoleAdmin);

    @SelectProvider(type = AdminSqlProvider.class, method = "buildSelectSql")
    List<DAdmin> selectAdminByAccount(DAdmin admin);

    @UpdateProvider(type = AdminSqlProvider.class, method = "buildUpdateSql")
    Integer update(TAdmin admin);

    @UpdateProvider(type = AdminSqlProvider.class, method = "updateRoleAdminByAdminIdSql")
    Integer updateRoleAdminByAdminId(TRoleAdmin tRoleAdmin);

    @UpdateProvider(type = AdminSqlProvider.class, method = "updateGroupAdminByAdminIdSql")
    Integer updateGroupAdminByAdminId(TGroupAdmin tGroupAdmin);

    @DeleteProvider(type = AdminSqlProvider.class, method = "buildDelGroupAdminSql")
    Integer delete_group_admin(TAdmin admin);

    @DeleteProvider(type = AdminSqlProvider.class, method = "buildDelRoleAdminSql")
    Integer delete_role_admin(TAdmin admin);

    @SelectProvider(type = AdminSqlProvider.class, method = "buildCountAdminSql")
    PageResponse searchAminLimit(@Param("data") DPageSearchData data, @Param("orgCode") String orgCode,@Param("companyId") Integer companyId, List<String> permissions);

    @SelectProvider(type = AdminSqlProvider.class, method = "buildSearchAdminPageSql")
    List<DPageRsultData> searchAdmin(@Param("data") DPageSearchData data,@Param("orgCode") String orgCode,@Param("index") Integer index,@Param("size") Integer size,@Param("companyId") Integer companyId, List<String> permissions);

    @SelectProvider(type = AdminSqlProvider.class, method = "buildExportAdminsSql")
    List<DUploadFileData> getExportAdminsData(@Param("companyId") Integer companyId,@Param("orgCode") String orgCode, List<String> permissions);

    @Select("select * from t_admin where id = #{id}")
    TAdmin get(@Param("id") Integer id);

    @Select("select * from t_admin where id <> #{id} and company_id =#{companyId} and account =#{account}")
    TAdmin getByIdAndCompanyId(@Param("id") Integer id,@Param("companyId") Integer companyId,@Param("account") String account);

    @Select("select * from t_admin where company_id =#{companyId} and account =#{account}")
    TAdmin getByAccountAndCompanyId(@Param("companyId") Integer companyId,@Param("account") String account);

    @Update("update t_admin set status=#{status},modified_date=now(),modifier=#{modifier} where id=#{id}")
    Integer updateAdminStatus(@Param("status") Integer status,@Param("modifier") String modifier,@Param("id") String id);

    @Update("update t_admin set password=#{password},modified_date=now(),modifier=#{modifier} where id =#{id}")
    Integer updateAdminPassword(@Param("password")  String password,@Param("modifier") String modifier,@Param("id") String id);

    @Select("select * from t_admin where id = #{id} and  password=#{password}")
    TAdmin getByPassword(@Param("id") Integer id,@Param("password") String password);

    @SelectProvider(type = AdminSqlProvider.class, method = "buildSelectAdminSql")
    DAdmin getDAdmin(Integer id);

    @SelectProvider(type = AdminSqlProvider.class, method = "getAdminsByAccounts")
    List<TAdmin> getAdminsByAccounts(@Param("companyId") Integer companyId, @Param("accounts") List<String> accounts);

    @SelectProvider(type = AdminSqlProvider.class, method = "buildSelectSubordinateSql")
    List<DSubordinate> getDSubordinate(@Param("companyId")Integer companyId, @Param("orgCode") String orgCode);

    @SelectProvider(type = AdminSqlProvider.class, method = "buildSelectExaminersSql")
    List<DSubordinate> getDExaminers(@Param("companyId")Integer companyId);
}
