package com.talebase.cloud.ms.admin.service;

import com.talebase.cloud.base.ms.admin.domain.*;
import com.talebase.cloud.base.ms.admin.dto.*;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.Des3Util;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.admin.dao.AdminMapper;
import com.talebase.cloud.ms.admin.dao.GroupMapper;
import com.talebase.cloud.ms.admin.dao.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by daorong.li on 2016-11-23.
 */

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private RoleMapper roleMapper;

    public TAdmin getAdmin(Integer id) {
        return adminMapper.get(id);
    }

    public TAdmin getByAccountAndCompanyId(Integer companyId, String account) {
        return adminMapper.getByAccountAndCompanyId(companyId, account);
    }

    public DAdmin getDAdmin(Integer id) {
        return adminMapper.getDAdmin(id);
    }

    public List<DSubordinate> getDSubordinate(Integer companyId, String orgCode) {
        return adminMapper.getDSubordinate(companyId, orgCode);
    }

    public List<DSubordinate> getDExaminers(Integer companyId) {
        return adminMapper.getDExaminers(companyId);
    }

    public List<DUploadFileData> getExportData(ServiceRequest<DAdmin> req) {
        DAdmin dAdmin = req.getRequest();
        Integer id = dAdmin.getId();
        String orgCode = dAdmin.getOrgCode();
        TAdmin admin = adminMapper.get(id);
        if (admin != null && admin.getCompanyId() > 0) {
            List<DUploadFileData> list = adminMapper.getExportAdminsData(admin.getCompanyId(), orgCode, req.getRequestHeader().getPermissions());
            return list;
        }
        return null;
    }

    /**
     * 新增管理员
     *
     * @param admin
     * @return
     */
    @Transactional(readOnly = false)
    public ServiceResponse<TAdmin> add(DAdmin admin) throws Exception {

        if (isExistAccount(admin)) {
            return new ServiceResponse(BizEnums.ADMIN_ADD_EXIST);
        }
        TGroup getGroup = groupMapper.getgroup(admin.getGroupId());
        TRole getRole = roleMapper.getrole(admin.getRoleId());
        if (getGroup == null) {
            return new ServiceResponse(BizEnums.ADMIN_ADD_EXIST);
        }
        if (getRole == null) {
            return new ServiceResponse(BizEnums.ADMIN_ADD_EXIST);
        }
        if (admin.getPassword().length() < 6) {
            throw new WrappedException(BizEnums.Password_TooShort);
        }
        String passWord = Des3Util.des3EncodeCBC(admin.getPassword());// 密码加密
        TAdmin tAdmin = new TAdmin();
        tAdmin.setAccount(admin.getAccount());
        tAdmin.setCompanyId(admin.getCompanyId());
        tAdmin.setCreatedDate(new Timestamp(new Date().getTime()));
        tAdmin.setCreater(admin.getCreater());
        tAdmin.setEmail(admin.getEmail());
        tAdmin.setOrgCode(getGroup.getOrgCode());
        tAdmin.setPassword(passWord);
        tAdmin.setMobile(admin.getMobile());
        tAdmin.setStatus(admin.getStatus());
        if (!StringUtil.isEmpty(admin.getName())) {
            tAdmin.setName(admin.getName());
        } else
            tAdmin.setName(admin.getAccount());
        adminMapper.insert(tAdmin);

        TGroupAdmin tGroupAdmin = new TGroupAdmin();
        tGroupAdmin.setAdminId(tAdmin.getId());
        tGroupAdmin.setGroupId(admin.getGroupId());

        adminMapper.insertGroupAdmin(tGroupAdmin);

        TRoleAdmin tRoleAdmin = new TRoleAdmin();
        tRoleAdmin.setAdminId(tAdmin.getId());
        tRoleAdmin.setRoleId(admin.getRoleId());
        adminMapper.insertRoleAdmin(tRoleAdmin);

        return new ServiceResponse(tAdmin);
    }

    /**
     * 修改管理员
     *
     * @param admin
     * @return
     */
    @Transactional(readOnly = false)
    public ServiceResponse update(TAdmin admin, Integer roleId, Integer groupId) {
        TGroup getGroup = groupMapper.getgroup(groupId);
        TRole getRole = roleMapper.getrole(roleId);
        if (getGroup == null) {
            return new ServiceResponse(BizEnums.ADMIN_ADD_EXIST);
        }
        if (getRole == null) {
            return new ServiceResponse(BizEnums.ADMIN_ADD_EXIST);
        }
        adminMapper.update(admin);

        if (roleId != null && admin.getId() != null) {
            TRoleAdmin tRoleAdmin = new TRoleAdmin();
            tRoleAdmin.setRoleId(roleId);
            tRoleAdmin.setAdminId(admin.getId());
            adminMapper.updateRoleAdminByAdminId(tRoleAdmin);
        }

        if (groupId != null && admin.getId() != null) {
            TGroupAdmin tGroupAdmin = new TGroupAdmin();
            tGroupAdmin.setAdminId(admin.getId());
            tGroupAdmin.setGroupId(groupId);
            Integer count = adminMapper.updateGroupAdminByAdminId(tGroupAdmin);
            if (count == null || count <= 0) {
                adminMapper.insertGroupAdmin(tGroupAdmin);
            }
        }
        return null;
    }

    /**
     * 先删除对应组、角色，再更新管理员状态为删除状态
     *
     * @param admin
     */
    @Transactional(readOnly = false)
    public void del(TAdmin admin) {
        adminMapper.delete_group_admin(admin);
        adminMapper.delete_role_admin(admin);
        adminMapper.update(admin);
    }

    public PageResponse searchAdmins(ServiceRequest<DPageSearchData> req) {
        DPageSearchData data = req.getRequest();
        String key = data.getKey().trim();
        data.setKey(key);
        ServiceHeader serviceHeader = req.getRequestHeader();
        int start = req.getPageReq().getStart();
        int limit = req.getPageReq().getLimit();

        PageResponse total = adminMapper.searchAminLimit(data, serviceHeader.getOrgCode(), req.getRequestHeader().companyId, req.getRequestHeader().getPermissions());
        List<DPageRsultData> pageList = adminMapper.searchAdmin(data, serviceHeader.getOrgCode(), start, limit, req.getRequestHeader().companyId, req.getRequestHeader().getPermissions());
        PageResponse page = new PageResponse();
        page.setStart(start);
        page.setLimit(limit);
        page.setTotal(total.getTotal());
        page.setResults(pageList);
        return page;
    }

    /**
     * 更新管理员状态
     *
     * @param status
     * @param ids
     * @return
     */
    @Transactional(readOnly = false)
    public void updateAdminStatus(Integer status, String modifier, String ids) {
        String[] arg = ids.split(",");
        for (int i = 0; i < arg.length; i++) {
            adminMapper.updateAdminStatus(status, modifier, arg[i]);
        }

    }

    /**
     * 更新管理员密码
     *
     * @param admin
     * @return
     */
    public ServiceResponse updateAdminPassword(DAdmin admin) throws Exception {
        if (StringUtils.isEmpty(admin.getPassword())) {
            throw new WrappedException(BizEnums.ADMIN_PASSWORD_NOEXIST);
        }
        if (admin.getPassword().length() > 50) {
            throw new WrappedException(BizEnums.ADMIN_LENGTH_TOO_LONG);
        }
        String passWord = Des3Util.des3EncodeCBC(admin.getPassword());// 密码加密
        //管理员修改自己密码
        if (!StringUtils.isEmpty(admin.getOldPassword()) && !StringUtils.isEmpty(admin.getId())) {

            String oldPassword = Des3Util.des3EncodeCBC(admin.getOldPassword());
            //TAdmin tAdmin = adminMapper.getByPassword(admin.getId(),admin.getOldPassword());
            TAdmin tAdmin = adminMapper.getByPassword(admin.getId(), oldPassword);
            if (tAdmin == null) {
                //return new ServiceResponse(BizEnums.ADMIN_OLDPASSWORD_NOEXIST);
                throw new WrappedException(BizEnums.ADMIN_OLDPASSWORD_NOEXIST);
            } else {
                if (admin.getPassword().equals(admin.getOldPassword())) {
                    throw new WrappedException(BizEnums.ADMIN_SAME_PASSWORD);
                }
            }
            adminMapper.updateAdminPassword(passWord, admin.getModifier(), admin.getId().toString());
        } else {//批量修改管理员密码
            String[] idAttr = admin.getIds().split(",");
            for (int i = 0; i < idAttr.length; i++) {
                adminMapper.updateAdminPassword(passWord, admin.getModifier(), idAttr[i]);
            }

        }

        return new ServiceResponse();
    }

    /**
     * 查找管理员是否存在
     *
     * @param admin
     * @return
     */
    public boolean isExistAccount(DAdmin admin) {
        List<DAdmin> list = adminMapper.selectAdminByAccount(admin);
        if (!list.isEmpty() && list.size() > 0)
            return true;
        return false;
    }

    /**
     * 判断是否存在管理员
     *
     * @param id
     * @return
     */
    public boolean isExistAccountById(Integer id, String account) {
        TAdmin admin = adminMapper.get(id);
        if (admin != null) {
            TAdmin tAdmin = adminMapper.getByIdAndCompanyId(admin.getId(), admin.getCompanyId(), account);
            if (tAdmin != null)
                return true;
            return false;
        }
        return false;
    }

    /**
     * 根据账号查询多个管理员
     *
     * @param accounts
     * @return
     */
    public List<TAdmin> getAdminsByAccounts(Integer companyId, List<String> accounts) {
        return adminMapper.getAdminsByAccounts(companyId, accounts);
    }


}
