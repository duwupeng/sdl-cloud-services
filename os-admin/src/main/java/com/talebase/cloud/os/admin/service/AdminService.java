package com.talebase.cloud.os.admin.service;

import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.admin.dto.DGroupAndRole;
import com.talebase.cloud.base.ms.admin.dto.DPageSearchData;
import com.talebase.cloud.base.ms.admin.dto.DSubordinate;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by daorong.li on 2016-11-23.
 */

@Service
public class AdminService {
    @Autowired
    MsInvoker msInvoker;

    final static String SERVICE_NAME = "ms-admin";

    /**
     * 获取管理员分页数据
     * @param req
     * @return
     */
    public ServiceResponse<PageResponse> getAdmins(ServiceRequest<DPageSearchData> req) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/getAdmins";
        ServiceResponse<PageResponse> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<PageResponse>>(){});
        return  response;
    }
    /**
     * 获取修改管理员信息
     * @param id
     * @return
     */
    public DAdmin getAdmin(Integer id) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/edit/{adminId}";
        ServiceResponse<DAdmin> response  = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){},id);
        return  response.getResponse();
    }
    /**
     * 获取下级管理员
     */
    public List<DSubordinate> getSubordinate(Integer companyId,String orgCode) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/subordinate?companyId={companyId}&orgCode={orgCode}";
        ServiceResponse<List<DSubordinate>> response  = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<DSubordinate>>>(){}, companyId, orgCode);
        return  response.getResponse();
    }
    /**
     * 查询考官
     */
    public List<DSubordinate> getExaminers(Integer companyId) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/examiners?companyId={companyId}";
        ServiceResponse<List<DSubordinate>> response  = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<DSubordinate>>>(){}, companyId);
        return  response.getResponse();
    }
    /**
     * 获取分制和角色
     * @param orgCode
     * @return
     */
    public ServiceResponse<DGroupAndRole> getGroupAndRoleForSelect(String orgCode,Integer companyId) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/getAdmins/"+companyId;
        ServiceRequest<String> req = new ServiceRequest<String>();
        req.setRequest(orgCode);
        ServiceResponse<DGroupAndRole> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<DGroupAndRole>>(){});
        return  response;
    }
    /**
     * 添加管理员
     * @param req
     * @return
     */
    public ServiceResponse saveAdmin(ServiceRequest<DAdmin> req) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/add";
        ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req, new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        return  response;
    }

    /**
     * 修改管理员
     * @param req
     * @return
     */
    public ServiceResponse<DAdmin> editAdmin(ServiceRequest<DAdmin> req) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/update";
        ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req, new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        return  response;
    }

    /**
     * 修改管理员
     * @param req
     * @return
     */
    public ServiceResponse editAdminStatus(ServiceRequest<DAdmin> req) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/setStatus";
        ServiceResponse response = msInvoker.post(servicePath,req, new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return  response;
    }

    /**
     * 重置密码
     * @param req
     * @return
     */
    public ServiceResponse editAdminPassword(ServiceRequest<DAdmin> req) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/reSetPassword";
        ServiceResponse response = msInvoker.post(servicePath,req, new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return  response;
    }

    /**
     * 删除管理员
     * @param req
     * @return
     */
    public ServiceResponse<DAdmin> delAdmin(ServiceRequest<DAdmin> req) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/del";
        ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req, new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        return  response;
    }
}
