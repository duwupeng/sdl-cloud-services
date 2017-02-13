package com.talebase.cloud.ms.admin.dao;

import com.talebase.cloud.base.ms.admin.domain.TPermission;
import com.talebase.cloud.base.ms.admin.dto.DPermissionReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-11-10.
 */
@Mapper
public interface PermissionMapper {

    @Select("select * from t_permission tp " +
            " left join t_role_permission trp" +
            " on tp.id=trp.permission_id " +
            " where trp.role_id=#{roleId}")
    List<TPermission> getPermissions(@Param("roleId") Integer roleId);


    @Select("select * from t_permission tp " +
            " left join t_role_permission trp" +
            " on tp.id=trp.permission_id " +
            " where trp.role_id in (" +
            " select role_id from t_role_admin where " +
            " admin_id=#{operatorId}" +
            ")")
    List<TPermission> getPermissionsByOperatorId(@Param("operatorId") Integer operatorId);

    @SelectProvider(type = PermissionSqlProvider.class, method = "findPermissions")
    List<TPermission> findPermissions(@Param("type") Integer type,@Param("noTypes") List<Integer> noTypes);
}
