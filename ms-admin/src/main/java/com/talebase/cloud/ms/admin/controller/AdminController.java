package com.talebase.cloud.ms.admin.controller;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.base.ms.admin.dto.*;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.admin.dao.GroupMapper;
import com.talebase.cloud.ms.admin.service.RoleService;
import com.talebase.cloud.ms.admin.service.AdminService;
import com.talebase.cloud.ms.admin.service.GroupService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by daorong.li on 2016-11-23.
 */

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    GroupService groupService;
    @Autowired
    RoleService roleService;
    @Autowired
    private GroupMapper groupMapper;

    @PostMapping(value = "/serviceAdmin/update")
    public ServiceResponse<DAdmin> update(@RequestBody ServiceRequest<DAdmin> req) {
        DAdmin admin = req.getRequest();
        //判断修改账号是否重复
        if (adminService.isExistAccountById(admin.getId(), admin.getAccount())) {
            return new ServiceResponse(admin, BizEnums.ADMIN_MODIFY_EXIST);
        }

        TAdmin tAdmin = new TAdmin();
        tAdmin.setId(admin.getId());
        //tAdmin.setAccount(admin.getAccount());
        tAdmin.setCompanyId(admin.getCompanyId());
        tAdmin.setModifiedDate(new Timestamp(new Date().getTime()));
        tAdmin.setModifier(admin.getModifier());
        tAdmin.setEmail(admin.getEmail());
        tAdmin.setName(admin.getName());
        tAdmin.setOrgCode(groupMapper.getgroup(admin.getGroupId()).getOrgCode());
       // tAdmin.setOrgCode(admin.getOrgCode());
        tAdmin.setPassword(admin.getPassword());
        tAdmin.setStatus(admin.getStatus());
        tAdmin.setMobile(admin.getMobile());

        adminService.update(tAdmin, admin.getRoleId(), admin.getGroupId());

        return new ServiceResponse(admin);

    }

    @PostMapping(value = "/serviceAdmin/reSetPassword")
    public ServiceResponse rePassword(@RequestBody ServiceRequest<DAdmin> req) throws Exception {
        DAdmin admin = req.getRequest();
        return adminService.updateAdminPassword(admin);
    }

    @PostMapping(value = "/serviceAdmin/setStatus")
    public ServiceResponse status(@RequestBody ServiceRequest<DAdmin> req) {
        DAdmin admin = req.getRequest();
        adminService.updateAdminStatus(admin.getStatus(), admin.getModifier(), admin.getIds());
        return new ServiceResponse();
    }

    @PostMapping(value = "/serviceAdmin/del")
    public ServiceResponse<DAdmin> del(@RequestBody ServiceRequest<DAdmin> req) {
        DAdmin admin = req.getRequest();

        TAdmin tAdmin = new TAdmin();
        tAdmin.setId(admin.getId());
        tAdmin.setModifier(admin.getModifier());
        tAdmin.setModifiedDate(new Timestamp(new Date().getTime()));
        tAdmin.setStatus(-1);

        adminService.del(tAdmin);

        return new ServiceResponse(admin);

    }

    @GetMapping(value = "/serviceAdmin/getAdmin/{id}")
    public ServiceResponse<TAdmin> getAdminById(@PathVariable("id") Integer id) {
        TAdmin tAdmin = adminService.getAdmin(id);
         return new ServiceResponse<>(tAdmin);
    }

    @PostMapping(value = "/serviceAdmin/getAdminsByAccounts")
    public ServiceResponse<List<TAdmin>> getAdminsByAccounts(@RequestBody ServiceRequest<List<String>> req) {
        List<TAdmin> list = adminService.getAdminsByAccounts(req.getRequestHeader().getCompanyId(), req.getRequest());
        return new ServiceResponse<>(list);
    }

    /**
     * 根据账号和公司ID获取管理员信息
     * @param req
     * @return
     */
    @PostMapping(value = "/serviceAdmin/getByAccountAndCompanyId")
    public ServiceResponse<TAdmin> getByAccountAndCompanyId(@RequestBody ServiceRequest<DAdmin> req) {
        DAdmin dAdmin = req.getRequest();
        TAdmin tAdmin = adminService.getByAccountAndCompanyId(dAdmin.getCompanyId(), dAdmin.getAccount());
        ServiceResponse<TAdmin> response = new ServiceResponse<TAdmin>(tAdmin);
        return response;
    }

    @PostMapping(value = "/serviceAdmin/add")
    public ServiceResponse<TAdmin> add(@RequestBody ServiceRequest<DAdmin> req) throws Exception {
        DAdmin admin = req.getRequest();
        return adminService.add(admin);
    }

    @GetMapping(value = "/serviceAdmin/subordinate")
    public ServiceResponse<List<DSubordinate>> getSubordinate(Integer companyId, String orgCode) {
        List<DSubordinate> list = adminService.getDSubordinate(companyId, orgCode);
        return new ServiceResponse(list);
    }

    @PostMapping(value = "/serviceAdmin/getAdmins/{companyId}")
    public ServiceResponse<DGroupAndRole> getGroupAndRoleForSelect(@RequestBody ServiceRequest<String> req, @PathVariable("companyId") Integer companyId) {
        DGroupAndRole dto = new DGroupAndRole();
        dto.setGroupList(groupService.findGroupsByOrgCode(companyId, req.getRequest()));
        dto.setRoleList(roleService.findCompanyRoles(companyId));
        return new ServiceResponse<>(dto);
    }

    @GetMapping(value = "/serviceAdmin/edit/{adminId}")
    public ServiceResponse<DAdmin> getEditAdmin(@PathVariable("adminId") Integer adminId) {
        DAdmin admin = adminService.getDAdmin(adminId);
        ServiceResponse<DAdmin> response = new ServiceResponse<DAdmin>(admin);
        return response;
    }

    @PostMapping(value = "/serviceAdmin/getAdmins")
    public ServiceResponse<PageResponse> getAdmins(@RequestBody ServiceRequest<DPageSearchData> req) {
        PageResponse page = adminService.searchAdmins(req);
        ServiceResponse<PageResponse> response = new ServiceResponse<PageResponse>(page);
        response.getResponse().setPageIndex(req.getPageReq().getPageIndex());
        req.getRequestHeader().getPermissions();
        return response;
    }

    /**
     * 获取导出数据
     * @param req
     * @return
     */
    //@GetMapping(value = "/serviceAdmin/getExportData")
    @PostMapping(value ="/serviceAdmin/getExportData")
    public ServiceResponse<List<DUploadFileData>> getExportAdminsData(@RequestBody ServiceRequest<DAdmin> req) {
        DAdmin dAdmin = req.getRequest();
        List<DUploadFileData> list = adminService.getExportData(req);
        return new ServiceResponse(list);
    }

    @GetMapping(value = "/serviceAdmin/examiners")
    public ServiceResponse<List<DSubordinate>> getExaminers(Integer companyId) {
        List<DSubordinate> list = adminService.getDExaminers(companyId);
        return new ServiceResponse(list);
    }

}
