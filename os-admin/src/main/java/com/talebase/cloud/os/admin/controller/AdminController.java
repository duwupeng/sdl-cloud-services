package com.talebase.cloud.os.admin.controller;

import com.netflix.discovery.converters.Auto;
import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.base.ms.admin.dto.DSubordinate;
import com.talebase.cloud.base.ms.admin.enums.PermissionCode;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.util.PasswordUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.os.admin.service.AdminService;
import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.admin.dto.DGroupAndRole;
import com.talebase.cloud.base.ms.admin.dto.DPageSearchData;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.os.admin.service.GroupService;
import com.talebase.cloud.os.admin.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daorong.li on 2016-11-23.
 * 管理员设置
 */

@RestController
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    GroupService groupService;
    @Autowired
    ProjectService projectService ;


    /**
     * 获取管理员列表
     * @param data
     * @param pageReq
     * @return
     */
    @GetMapping(value = "/admins")
    public ServiceResponse<PageResponseWithParam<PageResponse,DPageSearchData>> getAdmins(DPageSearchData data,PageRequest pageReq){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        //获取当前用户操作权限
//        boolean canEdit  = serviceHeader.hasRight(PermissionCode.ADMIN_LIST_RIGHT.getCode());
//        if (!canEdit){
//            serviceHeader.setOrgCode("");
//        }
        //设置请求头数据
        ServiceRequest<DPageSearchData> req = new ServiceRequest<DPageSearchData>();
        req.setRequest(data);
        req.setPageReq(pageReq);
        req.setRequestHeader(serviceHeader);

        //获取返回数据
        ServiceResponse<PageResponse> response = adminService.getAdmins(req);
        PageResponse pageResponse = response.getResponse();
        //获取分组和角色
        ServiceResponse<DGroupAndRole> grObj = adminService.getGroupAndRoleForSelect(serviceHeader.getOrgCode(),serviceHeader.getCompanyId());
        DGroupAndRole obj = grObj.getResponse();
        data.setdGroupAndRole(obj);
        //组装返回数据
        PageResponseWithParam<PageResponse,DPageSearchData> param = new PageResponseWithParam<PageResponse,DPageSearchData>(pageReq,data);
        param.setResults(pageResponse.getResults());
        param.setTotal(pageResponse.getTotal());
        //设置当前用户操作权限
        Map<String,Boolean> orgCode = new HashMap<String,Boolean>();
//        orgCode.put("canEdit",canEdit);//判断用户是否有编辑权限

        ServiceResponse<PageResponseWithParam<PageResponse,DPageSearchData>> pageResponseWithParam = new ServiceResponse<PageResponseWithParam<PageResponse,DPageSearchData>>(param);
        pageResponseWithParam.setPermission(orgCode);

        return pageResponseWithParam;
    }

    /**
     * 获取管理员列表中的角色和分组列表
     * @return
     */
    @GetMapping(value = "/roleAndGroup")
    public ServiceResponse<DGroupAndRole> getRoleAndGroup(){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        //获取当前用户操作权限
//        boolean canEdit  = serviceHeader.hasRight(PermissionCode.ADMIN_LIST_RIGHT.getCode());
//        if (!canEdit){
//            serviceHeader.setOrgCode("");
//        }
        ServiceResponse<DGroupAndRole> grObj = adminService.getGroupAndRoleForSelect(serviceHeader.getOrgCode(),serviceHeader.getCompanyId());
        return  grObj;
    }

    /**
     * 获取编辑管理员
     * @param adminId
     * @return
     */
    @GetMapping(value="/admin/edit/{adminId}")
    public ServiceResponse<DAdmin> getAdmin(@PathVariable("adminId") Integer adminId){
/*        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        //获取当前用户操作权限
        boolean canEdit  = serviceHeader.hasRight(PermissionCode.ADMIN_LIST_RIGHT.getCode());
        if (!canEdit){
            serviceHeader.setOrgCode("");
        }
        //获取分组和角色
        ServiceResponse<DGroupAndRole> grObj = adminService.getGroupAndRoleForSelect(serviceHeader.getOrgCode(),companyId);
        DGroupAndRole dGroupAndRole = grObj.getResponse();
        DAdmin admin = adminService.getAdmin(adminId);
        DAdminAndGroupAndRole obj = new DAdminAndGroupAndRole(admin,dGroupAndRole);
        ServiceResponse<DAdminAndGroupAndRole> response = new ServiceResponse<DAdminAndGroupAndRole>(obj);*/
        DAdmin admin = adminService.getAdmin(adminId);
        ServiceResponse<DAdmin> response = new ServiceResponse<DAdmin>(admin);
        return  response;
    }

    /**
     * 获取下级管理员
     */
    @GetMapping(value = "/subordinate")
    public ServiceResponse<List<DSubordinate>> getSubordinate(){
        Integer companyId = ServiceHeaderUtil.getRequestHeader().getCompanyId();
        String orgCode = ServiceHeaderUtil.getRequestHeader().getOrgCode();
        List<DSubordinate> list = adminService.getSubordinate(companyId,orgCode);
        return new ServiceResponse<>(list);
    }

    /**
     * 查询考官
     */
    @GetMapping(value = "/examiners")
    public ServiceResponse<List<DSubordinate>> getExaminers(Integer companyId){
        companyId = companyId == null ? ServiceHeaderUtil.getRequestHeader().getCompanyId() : companyId;
        List<DSubordinate> list = adminService.getExaminers(companyId);
        return new ServiceResponse<>(list);
    }
    /**
     * 添加管理员
     * @param dAdmin
     * @return
     */
    @PostMapping(value="/admin")
    public ServiceResponse saveAdmin(DAdmin dAdmin){
        if(StringUtils.isEmpty(dAdmin.getPassword())){
            throw new WrappedException(BizEnums.ADMIN_CREATE_PASSWORD_NOEXIST);
        }
        if (dAdmin.getPassword().length() <6){
            throw new WrappedException(BizEnums.ADMIN_CREATE_Password_TooShort);
        }
        if (!PasswordUtil.containLetterAndNumber(dAdmin.getPassword())){
            throw new WrappedException(BizEnums.ADMIN_CREATE_Password_NotVail);
        }
        if (PasswordUtil.containChinese(dAdmin.getPassword())){
            throw new WrappedException(BizEnums.ADMIN_CREATE_Password_CannotHas_Chinese);
        }
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dAdmin.setCompanyId(serviceHeader.getCompanyId());
        dAdmin.setCreater(serviceHeader.getOperatorName());
        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(dAdmin);
        ServiceResponse serviceResponse = adminService.saveAdmin(req);

        saveProjectGroupAdmin(dAdmin);

        return serviceResponse;
    }

    /**
     * 修改管理员
     * @param dAdmin
     * @return
     */
    @PutMapping(value = "/admin")
    public ServiceResponse<DAdmin> editAdmin(DAdmin dAdmin){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dAdmin.setModifier(serviceHeader.getOperatorName());
        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(dAdmin);
        ServiceResponse serviceResponse = adminService.editAdmin(req);

        saveProjectGroupAdmin(dAdmin);

        return serviceResponse;
    }

    /**
     * 修改管理员状态
     * @param dAdmin
     * @param dAdmin
     * @return
     */
    @PutMapping(value = "/admin/status")
    public ServiceResponse editAdminStatus(DAdmin dAdmin){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dAdmin.setModifier(serviceHeader.getOperatorName());
        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(dAdmin);
        return  adminService.editAdminStatus(req);
    }

    /**
     * 重置密码
     * @param dAdmin
     * @return
     */
    @PutMapping(value = "/admin/password")
    public ServiceResponse editAdminPassword(DAdmin dAdmin){
        if(StringUtils.isEmpty(dAdmin.getPassword())){
            throw new WrappedException(BizEnums.ADMIN_PASSWORD_NOEXIST);
        }
        if (dAdmin.getPassword().length() <6){
            throw new WrappedException(BizEnums.Password_TooShort);
        }
        if (!PasswordUtil.containLetterAndNumber(dAdmin.getPassword())){
            throw new WrappedException(BizEnums.Password_NotVail);
        }
        if (PasswordUtil.containChinese(dAdmin.getPassword())){
            throw new WrappedException(BizEnums.Password_CannotHas_Chinese);
        }
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dAdmin.setModifier(serviceHeader.getOperatorName());
        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(dAdmin);
        return  adminService.editAdminPassword(req);
    }

    /**
     * 删除管理员
     * @param dAdmin
     * @return
     */
    @DeleteMapping(value ="/admin" )
    public ServiceResponse<DAdmin> delAdmin(DAdmin dAdmin){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dAdmin.setModifier(serviceHeader.getOperatorName());
        ServiceRequest<DAdmin> req = new  ServiceRequest<DAdmin>();
        req.setRequest(dAdmin);
        return  adminService.delAdmin(req);
    }

    private void saveProjectGroupAdmin(DAdmin dAdmin){
        TGroup group = groupService.getGroup(dAdmin.getGroupId());
        if(StringUtil.isEmpty(dAdmin.getAccount()))
            dAdmin = adminService.getAdmin(dAdmin.getId());
        TAdmin admin = new TAdmin();
        admin.setCompanyId(ServiceHeaderUtil.getRequestHeader().getCompanyId());
        admin.setOrgCode(group.getOrgCode());
        admin.setAccount(dAdmin.getAccount());
        projectService.saveProjectGroupAdmin(admin, ServiceHeaderUtil.getRequestHeader());
    }

}
