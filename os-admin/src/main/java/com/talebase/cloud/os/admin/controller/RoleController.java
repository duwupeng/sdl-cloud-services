package com.talebase.cloud.os.admin.controller;

import com.talebase.cloud.base.ms.admin.dto.DPer;
import com.talebase.cloud.base.ms.admin.dto.DPermissionOfRole;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.admin.service.RoleService;
import com.talebase.cloud.base.ms.admin.dto.DRole;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kanghongzhao on 16/11/27.
 */
@RestController
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @PostMapping("/role")
    public ServiceResponse addRole(String name){
        return roleService.addRole(name);
    }

    @PutMapping("/role/name/{roleId}")
    public ServiceResponse updateRoleName(String accessToken, @PathVariable("roleId") Integer roleId, String newName){
        return roleService.updateRoleName(roleId, newName);
    }

    @DeleteMapping("/roles/delete")
    public ServiceResponse deleteRole(String delIdsStr){
        return roleService.deleteRoles(StringUtil.toIntListByComma(delIdsStr));
    }

    @GetMapping("/roles/query")
    public ServiceResponse<List<DRole>> findByCompany(Integer companyId){

        ServiceResponse serviceResponse = roleService.findByCompany(companyId);
        serviceResponse.setPermission(new HashMap<>());
        if(1 == 1){//有功能权限
            serviceResponse.getPermission().put("operatePermission", true);
        }else {
            serviceResponse.getPermission().put("operatePermission", false);
        }
        return serviceResponse;
    }

    @GetMapping("/role/permissionOfRole/{roleId}")
    public ServiceResponse<List<DPer>> findPermissionOfRole(@PathVariable("roleId") Integer roleId){
        ServiceResponse<List<DPermissionOfRole>> serviceResponse = roleService.findPermissionOfRole(roleId);
        List<DPer> lists=new ArrayList<>();
        DPer dper=null;
        for(DPermissionOfRole dpor:serviceResponse.getResponse()){
            if(dpor.getType() == 4 || dpor.getType() == 99 || dpor.getType() == 5)//顾问权限、考官权限需要被过滤,基础权限默认选择
                continue;

            if(dper == null || !dpor.getType().equals(dper.getType())){
                dper = new DPer();
                dper.setType(dpor.getType());
                dper.setPermissions(new ArrayList<>());
                lists.add(dper);
            }
            dper.getPermissions().add(dpor);
        }
        return new ServiceResponse<>(lists);
        }


    @PutMapping("/role/permissions/{roleId}")
    public ServiceResponse updateRolePermission(String permissionIdsStr, @PathVariable("roleId") Integer roleId){
        return roleService.updaterolePermission(roleId, StringUtil.toIntListByComma(permissionIdsStr));
    }

    @GetMapping("/roles/queryByPage")
    public ServiceResponse<PageResponse<DRole>> queryByPage(PageRequest pageRequest){

        ServiceResponse<PageResponse<DRole>> serviceResponse = roleService.findByCompanyByPage(ServiceHeaderUtil.getRequestHeader().getCompanyId(), pageRequest);
        serviceResponse.setPermission(new HashMap<>());
        if(1 == 1){//有功能权限
            serviceResponse.getPermission().put("operatePermission", true);
        }else {
            serviceResponse.getPermission().put("operatePermission", false);
        }
        return serviceResponse;
    }

}
