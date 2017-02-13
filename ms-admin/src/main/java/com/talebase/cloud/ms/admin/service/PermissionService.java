package com.talebase.cloud.ms.admin.service;

import com.talebase.cloud.base.ms.admin.domain.TPermission;
import com.talebase.cloud.ms.admin.dao.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by eric.du on 2016-11-29.
 */
@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    public List<TPermission> findPermissions(Integer roleId){
        return permissionMapper.getPermissions(roleId);
    }

    public List<TPermission> findPermissionsByOperatorId(Integer operatorId){
        return permissionMapper.getPermissionsByOperatorId(operatorId);
    }

//    public List<TPermission> getPermissionsByOperatorId(Integer roleId){
//        return permissionMapper.getPermissionsByOperatorId(roleId);
//    }

}
