package com.talebase.cloud.os.admin.service;

import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by daorong.li on 2016-11-23.
 */

@Service
public class PermissionService {
    @Autowired
    MsInvoker msInvoker;

    final static java.lang.String  SERVICE_NAME = "ms-admin";

    /**
     * 获取角色的全部权限
     * @return
     */
    public ServiceResponse<List<java.lang.String>> getPermissionsByRoleId(Integer roleId) {
        java.lang.String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/permissions/role/"+roleId;

        ServiceResponse<List<java.lang.String>> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<java.lang.String>>>(){},new Object());
        return  response;
    }


    /**
     * 获取用户的全部权限
     * @return
     */
    public ServiceResponse<List<java.lang.String>> getPermissionsByOperatorId(Integer operatorId) {
        java.lang.String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/permissions/operator/"+operatorId;

        ServiceResponse<List<java.lang.String>> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<java.lang.String>>>(){},new Object());
        return  response;
    }
}
