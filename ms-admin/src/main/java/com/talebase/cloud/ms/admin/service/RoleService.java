package com.talebase.cloud.ms.admin.service;

import com.talebase.cloud.base.ms.admin.domain.TPermission;
import com.talebase.cloud.base.ms.admin.domain.TRole;
import com.talebase.cloud.base.ms.admin.domain.TRolePermission;
import com.talebase.cloud.base.ms.admin.dto.DPermissionOfRole;
import com.talebase.cloud.base.ms.admin.dto.DPermissionReq;
import com.talebase.cloud.base.ms.admin.dto.DRole;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.ms.admin.dao.PermissionMapper;
import com.talebase.cloud.ms.admin.dao.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.talebase.cloud.ms.admin.MsAdminApplication.EXAMINER;
import static com.talebase.cloud.ms.admin.MsAdminApplication.OUTERADMIN;
import static com.talebase.cloud.ms.admin.MsAdminApplication.SUPERADMIN;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 更新角色名
     * @param operatorName
     * @param companyId
     * @param roleId
     * @param newRoleName
     */
    public void updateRoleName(String operatorName, Integer companyId, Integer roleId, String newRoleName){
        checkRoleNameExists(companyId, newRoleName,roleId);

        if(isDefaultRole(newRoleName)){
            throw new WrappedException(BizEnums.DefaultRoleCannotModify);
        }

        Integer updateRows = roleMapper.updateRoleName(newRoleName, operatorName, companyId, roleId);
        if(updateRows == 0){
            throw new WrappedException(BizEnums.RoleNameUpdateNoResult);
        }
    }

    static List<String> defaultRoles = Arrays.asList(SUPERADMIN, EXAMINER, OUTERADMIN);

    private boolean isDefaultRole(String roleName){
        return defaultRoles.contains(roleName);
    }

    /**
     * 检查组名唯一
     * @param companyId
     * @param roleName
     */
    private void checkRoleNameExists(Integer companyId, String roleName,Integer roleId){
        if(roleNameExists(companyId, roleName)& !roleIdSame(companyId, roleName,roleId)){
            throw new WrappedException(BizEnums.RoleNameRepeat);
        }
    }

    public Boolean roleNameExists(Integer companyId, String roleName){
        return roleMapper.getCntByName(companyId, roleName) > 0;
    }
    public Boolean roleIdSame(Integer companyId, String roleName,Integer roleId){
        return roleMapper.updateRoleNameSameId(companyId, roleName,roleId) > 0;
    }

    /**
     * 删除角色
     * 若角色中有管理员，或者包含系统角色，则返回错误提示
     * @param operatorName
     * @param companyId
     * @param roleIds
     */
    public void deleteRoles(String operatorName, Integer companyId, List<Integer> roleIds){

        List<TRole> rolesToDel = roleMapper.findRolesByIds(companyId, roleIds);
        List<String> defaultRoles = Arrays.asList(SUPERADMIN, EXAMINER, OUTERADMIN);

        for(TRole role : rolesToDel){
            if(defaultRoles.contains(role.getName()))
                throw new WrappedException(BizEnums.RoleCannotDelete);
        }

        Integer adminCntInRoleToDel = roleMapper.findAdminCntByRoles(roleIds);
        if(adminCntInRoleToDel > 0){
            throw new WrappedException(BizEnums.HasAdminRoleDelete);
        }

        roleMapper.updateStatusToDelete(roleIds, companyId, operatorName);
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    public Integer addRole(TRole role){
        if(isDefaultRole(role.getName())){
            throw new WrappedException(BizEnums.RoleNameIsDefault);
        }

        checkRoleNameExists(role.getCompanyId(), role.getName(),role.getId());

        roleMapper.insert(role);

        TRolePermission rolePermission = new TRolePermission();
        rolePermission.setRoleId(role.getId());
        roleMapper.insertDefaultRolePermission(rolePermission);

        return role.getId();
    }

    /**
     * 系统新增角色
     * @param role
     * @return
     */
    public Integer addRoleBySys(TRole role){
        roleMapper.insert(role);
        List<TPermission> list = null;
        if(OUTERADMIN.equals(role.getName())){
            list = permissionMapper.findPermissions(0,null);
        }else if(EXAMINER.equals(role.getName())){
            list = permissionMapper.findPermissions(4,null);
        }else if(SUPERADMIN.equals(role.getName())){
            List noTypes = new ArrayList<Integer>();
            noTypes.add(4);
            noTypes.add(99);
            list = permissionMapper.findPermissions(0,noTypes);
        }

        if(list != null && list.size() > 0){
            for(TPermission tPermission : list){
                TRolePermission rolePermission = new TRolePermission();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermissionId(tPermission.getId());
                roleMapper.insertRolePermission(rolePermission);
            }
        }

        return role.getId();
    }

    public List<TRole> findRoles(Integer companyId){
        return roleMapper.findRoles(companyId);
    }

    /**
     * 查询全部角色
     * 含角色所有管理员数量
     * @param companyId
     * @return
     */
    public List<DRole> findRolesWithAdminCnt(Integer companyId,List<String> permissions,String orgCode){
        return roleMapper.findRolesWithAdminCnt(companyId,permissions,orgCode);
    }

    /**
     * 更新用户权限
     * 全删全增逻辑
     * @param roleId
     * @param operatorName
     * @param permissionIds
     * @return
     */
    public Integer updateRolePermission(Integer roleId, String operatorName, List<Integer> permissionIds){

        roleMapper.deleteRolePermissions(roleId);

        TRolePermission roleDefaultPermission = new TRolePermission();
        roleDefaultPermission.setRoleId(roleId);
        roleMapper.insertDefaultRolePermission(roleDefaultPermission);

        for(Integer permissionId : permissionIds){
            TRolePermission rolePermission = new TRolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);

            roleMapper.insertRolePermission(rolePermission);
        }

        roleMapper.updateRoleModifyRelate(roleId, operatorName);

        return permissionIds.size();
    }

    /**
     * 查询全部权限(根据角色)
     * @param roleId
     * @return
     */
    public List<DPermissionOfRole> findPermissionsOfRole(Integer roleId){
        return roleMapper.findPermissionOfRole(roleId);
    }

    public List<TRole> findCompanyRoles(Integer companyId){
        return roleMapper.findCompanyRoles(companyId);
    }

    public List<DRole> findALLCompanyRoles(Integer companyId){
        return roleMapper.findALLCompanyRoles(companyId);
    }

    public PageResponse<DRole> queryPage(Integer companyId, PageRequest pageReq,List<String> permissions,String orgCode){

        PageResponse<DRole> pageResponse = new PageResponse<>(pageReq);

        Integer total = roleMapper.findRolesWithAdminCntNum(companyId,permissions,orgCode);
        pageResponse.setTotal(total);
        if(total > 0){
            List<DRole> list = roleMapper.findRolesWithAdminCntByPage(companyId, pageReq,permissions,orgCode);
            pageResponse.setResults(list);
        }

        return pageResponse;
    }
}
