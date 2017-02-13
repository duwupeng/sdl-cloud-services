package com.talebase.cloud.ms.admin.dao;

import com.talebase.cloud.base.ms.admin.domain.TRole;
import com.talebase.cloud.base.ms.admin.dto.DPermissionReq;
import com.talebase.cloud.base.ms.admin.enums.TRoleStatus;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

import static com.talebase.cloud.ms.admin.MsAdminApplication.OUTERADMIN;

/**
 * Created by zhangchunlin on 2017-1-12.
 */
public class PermissionSqlProvider {

    public String findPermissions(Integer type,List<Integer> noTypes){
        return new SQL(){{
            SELECT("*");
            FROM("t_permission");
            if(type != null && type != 0){
                WHERE("type =#{type}");
            }
            if(noTypes != null && noTypes.size() > 0){
                WHERE("type " + SqlBuilderUtil.buildNotInSql("noTypes", noTypes.size()));
            }
        }}.toString();
    }

}
