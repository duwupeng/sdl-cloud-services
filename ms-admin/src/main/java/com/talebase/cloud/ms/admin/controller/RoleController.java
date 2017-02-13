package com.talebase.cloud.ms.admin.controller;

import com.talebase.cloud.base.ms.admin.domain.TRole;
import com.talebase.cloud.base.ms.admin.dto.DPermissionOfRole;
import com.talebase.cloud.base.ms.admin.dto.DPermissionReq;
import com.talebase.cloud.base.ms.admin.dto.DRole;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by kanghongzhao on 16/11/26.
 */
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/serviceAdmin/role")
    public ServiceResponse addRole(@RequestBody ServiceRequest<String> serviceRequest){
        TRole role = new TRole();
        role.setName(serviceRequest.getRequest());
        role.setCreater(serviceRequest.getRequestHeader().operatorName);
        role.setModifier(serviceRequest.getRequestHeader().operatorName);
        role.setCompanyId(serviceRequest.getRequestHeader().companyId);
        roleService.addRole(role);
        return new ServiceResponse();
    }

    @PostMapping("/serviceAdmin/addRoleBySys")
    public ServiceResponse<Integer> addRoleBySys(@RequestBody ServiceRequest<DRole> serviceRequest){
        TRole role = new TRole();
        role.setName(serviceRequest.getRequest().getName());
        role.setCreater("system");
        role.setModifier("system");
        role.setCompanyId(serviceRequest.getRequest().getCompanyId());
        return new ServiceResponse(roleService.addRoleBySys(role));
    }

    @PutMapping("/serviceAdmin/role/name/{roleId}")
    public ServiceResponse updateRoleName(@PathVariable("roleId") Integer roleId, @RequestBody ServiceRequest<String> serviceRequest){
        roleService.updateRoleName(serviceRequest.getRequestHeader().operatorName, serviceRequest.getRequestHeader().companyId,
                roleId, serviceRequest.getRequest());
        return new ServiceResponse();
    }

    @DeleteMapping("/serviceAdmin/roles/delete")
    public ServiceResponse deleteRole(@RequestBody ServiceRequest<List<Integer>> serviceRequest){
        roleService.deleteRoles(serviceRequest.getRequestHeader().operatorName, serviceRequest.getRequestHeader().companyId, serviceRequest.getRequest());
        return new ServiceResponse();
    }

    @PostMapping("/serviceAdmin/roles/query")
    public ServiceResponse<List<DRole>> findByCompany(@RequestBody ServiceRequest<Integer> serviceRequest){
        List<DRole> roles = roleService.findRolesWithAdminCnt(serviceRequest.getRequest(),serviceRequest.getRequestHeader().getPermissions(),serviceRequest.getRequestHeader().getOrgCode());
        return new ServiceResponse(roles);
    }

    @PostMapping("/serviceAdmin/roles/findALLCompanyRoles")
    public ServiceResponse<List<DRole>> findALLCompanyRoles(@RequestBody ServiceRequest<Integer> serviceRequest){
        List<DRole> roles = roleService.findALLCompanyRoles(serviceRequest.getRequest());
        return new ServiceResponse(roles);
    }

    @GetMapping("/serviceAdmin/role/permissionOfRole/{roleId}")
    public ServiceResponse<List<DPermissionOfRole>> findPermissionOfRole(@PathVariable("roleId") Integer roleId){
        return new ServiceResponse(roleService.findPermissionsOfRole(roleId));
    }

    @PostMapping("/serviceAdmin/role/permissions/{roleId}")
    public ServiceResponse updateRolePermission(@PathVariable("roleId") Integer roleId, @RequestBody ServiceRequest<List<Integer>> serviceRequest){
        roleService.updateRolePermission(roleId,
                serviceRequest.getRequestHeader().operatorName,
                serviceRequest.getRequest());
        return new ServiceResponse();
    }

    @PostMapping("/serviceAdmin/roles/queryPage")
    public ServiceResponse<PageResponse<DRole>> queryPage(@RequestBody ServiceRequest<Integer> serviceRequest){
        PageResponse<DRole> pageResponse = roleService.queryPage(serviceRequest.getRequest(), serviceRequest.getPageReq(),serviceRequest.getRequestHeader().getPermissions(),serviceRequest.getRequestHeader().getOrgCode());
        return new ServiceResponse(pageResponse);
    }
}
