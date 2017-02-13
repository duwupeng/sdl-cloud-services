package com.talebase.cloud.os.admin.controller;

import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.os.admin.service.PermissionService;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by eric.du on 2016-11-29.
 * 数据权限
 */

@RestController
public class PermissionController {
    @Autowired
    PermissionService permissionService;
    /**
     * 获取权限列表
     * @return
     */
    @GetMapping(value = "/permissions/role/{roleId}")
    public ServiceResponse<List<java.lang.String>> getPermissions(@PathVariable("roleId") Integer roleId){
//        Cookie[] cooks = ServiceHeaderUtil.getRequest().getCookies();
        ServiceHeader serviceHeader =ServiceHeaderUtil.getRequestHeader();
        System.out.println(serviceHeader.getTransactionId());
        return  permissionService.getPermissionsByRoleId(roleId);
    }


    /**
     * 获取权限列表
     * @return
     */
    @GetMapping(value = "/permissions/operator/{operatorId}")
    public ServiceResponse<List<java.lang.String>> getPermissionsByOperatorId(@PathVariable("operatorId") Integer operatorId){
//        Cookie[] cooks = ServiceHeaderUtil.getRequest().getCookies();
        ServiceHeader serviceHeader =ServiceHeaderUtil.getRequestHeader();
        System.out.println(serviceHeader.getTransactionId());
        return  permissionService.getPermissionsByOperatorId(operatorId);
    }



    @GetMapping(value = "/exception/test")
    public ServiceResponse testException(){
           throw new WrappedException(BizEnums.ADMIN_ADD_EXIST);
    }

}
