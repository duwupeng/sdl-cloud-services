package com.talebase.cloud.os.admin.service;

import com.talebase.cloud.base.ms.admin.dto.DPermissionOfRole;
import com.talebase.cloud.base.ms.admin.dto.DRole;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kanghongzhao on 16/11/27.
 */
@Service
public class RoleService {

    @Autowired
    private MsInvoker msInvoker;

    final static String SERVICE_NAME = "ms-admin";

    public ServiceResponse updateRoleName(Integer roleId, String newName){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/role/name/" + roleId;
        ServiceResponse response = msInvoker.put(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), newName), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse deleteRoles(List<Integer> delIds){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/roles/delete";
        ServiceResponse response = msInvoker.delete(servicePath, new ServiceRequest<List<Integer>>(ServiceHeaderUtil.getRequestHeader(), delIds), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse addRole(String name){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/role";
        ServiceResponse response = msInvoker.post(servicePath, new ServiceRequest<String>(ServiceHeaderUtil.getRequestHeader(), name), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse<List<DRole>> findByCompany(Integer companyId){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/roles/query";
        ServiceResponse<List<DRole>> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), companyId), new ParameterizedTypeReference<ServiceResponse<List<DRole>>>(){});
        return response;
    }

    public ServiceResponse<PageResponse<DRole>> findByCompanyByPage(Integer companyId, PageRequest pageRequest){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/roles/queryPage";
        ServiceResponse<PageResponse<DRole>> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), companyId, pageRequest), new ParameterizedTypeReference<ServiceResponse<PageResponse<DRole>>>(){});
        return response;
    }

    public ServiceResponse updaterolePermission(Integer roleId, List<Integer> permissionIds){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/role/permissions/" + roleId;
        ServiceResponse response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), permissionIds), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse<List<DPermissionOfRole>> findPermissionOfRole(Integer roleId){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/role/permissionOfRole/{roleId}";
        ServiceResponse<List<DPermissionOfRole>> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<DPermissionOfRole>>>(){}, roleId);
        return response;
    }
}
