package com.talebase.cloud.ms.admin.dao;

import com.talebase.cloud.base.ms.admin.domain.TRole;
import com.talebase.cloud.base.ms.admin.domain.TRolePermission;
import com.talebase.cloud.base.ms.admin.dto.DPermissionOfRole;
import com.talebase.cloud.base.ms.admin.dto.DRole;
import com.talebase.cloud.common.protocal.PageRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
@Mapper
public interface RoleMapper {

    @Select("select count(*) from t_role where company_id = #{companyId} and name = #{name} and status != -1")
    Integer getCntByName(@Param("companyId") Integer companyId, @Param("name") String name);

    @SelectProvider(type = RoleSqlProvider.class, method = "updateRoleNameSameId")
    Integer updateRoleNameSameId(@Param("companyId") Integer companyId, @Param("name") String name,@Param("roleId") Integer roleId);

    @Update("update t_role set name = #{roleName}, modifier = #{operatorName}, modified_date = now() where company_id = #{companyId} and id = #{roleId}")
    Integer updateRoleName(@Param("roleName") String roleName, @Param("operatorName") String operatorName, @Param("companyId") Integer companyId, @Param("roleId") Integer roleId);

    @UpdateProvider(type = RoleSqlProvider.class, method = "updateStatusToDelete")
    Integer updateStatusToDelete(@Param("roleIds") List<Integer> roleIds, @Param("companyId") Integer companyId, @Param("operatorName") String operatorName);

    @InsertProvider(type = RoleSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TRole role);

    @Select("select * from t_role where id = #{id}")
    TRole get(Integer id);

    @Select("select * from t_role where companyId = #{companyId}")
    List<TRole> findRoles(Integer companyId);

    @SelectProvider(type = RoleSqlProvider.class, method = "findRolesByIds")
    List<TRole> findRolesByIds(@Param("companyId") Integer companyId, @Param("roleIds") List<Integer> roleIds);

    @SelectProvider(type = RoleSqlProvider.class, method = "findAdminCntByRoles")
    Integer findAdminCntByRoles(@Param("roleIds") List<Integer> roleIds);

    @SelectProvider(type = RoleSqlProvider.class, method = "findRolesWithAdminCnt")
    List<DRole> findRolesWithAdminCnt(@Param("companyId") Integer companyId,List<String> permissions,@Param("orgCode") String orgCode);

    @SelectProvider(type = RoleSqlProvider.class, method = "findRolesWithAdminCntNum")
    Integer findRolesWithAdminCntNum(@Param("companyId") Integer companyId,List<String> permissions,@Param("orgCode") String orgCode);

    @SelectProvider(type = RoleSqlProvider.class, method = "findRolesWithAdminCntByPage")
    List<DRole> findRolesWithAdminCntByPage(@Param("companyId") Integer companyId, @Param("pageReq") PageRequest pageReq,List<String> permissions,@Param("orgCode") String orgCode);

    @Select("select p.id, p.name, p.type,\n" +
            "case when rp.id is not null then true else false end as has_permission \n" +
            "from t_permission p \n" +
            "left join t_role_permission rp on p.id = rp.permission_id and rp.role_id = #{roleId} \n" +
//            "where rp.role_id = #{roleId} \n" +
            "order by p.type asc, p.id asc ")
    List<DPermissionOfRole> findPermissionOfRole(Integer roleId);

    @Delete("delete from t_role_permission where role_id = #{roleId}")
    Integer deleteRolePermissions(Integer roleId);

    @Insert("insert into t_role_permission(role_id, permission_id) values(#{roleId}, #{permissionId})")
    Integer insertRolePermission(TRolePermission rolePermission);

    @Insert("insert into t_role_permission(role_id, permission_id) values(#{roleId}, '34')")
    Integer insertDefaultRolePermission(TRolePermission rolePermission);

    @Update("update t_role set modifier = #{operatorName}, modified_date = now() where id = #{roleId}")
    Integer updateRoleModifyRelate(@Param("roleId") Integer roleId, @Param("operatorName") String operatorName);

    @SelectProvider(type = RoleSqlProvider.class, method = "findCompanyRoles")
    List<TRole> findCompanyRoles(Integer companyId);

    @SelectProvider(type = RoleSqlProvider.class, method = "findALLCompanyRoles")
    List<DRole> findALLCompanyRoles(Integer companyId);

    @Select("select * from t_role where id = #{id} and status=1")
    TRole getrole(@Param("id") Integer id);
}
