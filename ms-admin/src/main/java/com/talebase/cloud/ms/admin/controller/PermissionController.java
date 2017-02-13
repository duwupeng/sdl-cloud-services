package com.talebase.cloud.ms.admin.controller;

import com.talebase.cloud.base.ms.admin.domain.TPermission;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.admin.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by eric.du on 2016-11-25.
 */
@RestController
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/serviceAdmin/permissions/role/{roleId}")
    public ServiceResponse<List<java.lang.String> > getPermissionsByRoleId(@PathVariable("roleId") Integer roleId){
        List<TPermission> permissions = permissionService.findPermissions(roleId);
        List<java.lang.String> s = permissions.stream().map(TPermission::getCode).collect(Collectors.toList());
        return new ServiceResponse(s);
    }

    @GetMapping("/serviceAdmin/permissions/admin/{operatorId}")
    public ServiceResponse<List<java.lang.String> > findPermissionsByOperatorId(@PathVariable("operatorId") Integer operatorId){
        List<TPermission> permissions = permissionService.findPermissionsByOperatorId(operatorId);
        List<java.lang.String> s = permissions.stream().map(TPermission::getCode).collect(Collectors.toList());
        return new ServiceResponse(s);
    }

//    @GetMapping("/serviceAdmin/permissions/operator/{operatorId}")
//    public ServiceResponse<List<java.lang.String> > getPermissionsByOperatorId(@PathVariable("operatorId") Integer operatorId){
//        List<TPermission> permissions = permissionService.getPermissionsByOperatorId(operatorId);
//        List<java.lang.String> s = permissions.stream().map(TPermission::getCode).collect(Collectors.toList());
//        return new ServiceResponse(s);
//    }
}
