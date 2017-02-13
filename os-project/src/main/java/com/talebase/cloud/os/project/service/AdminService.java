package com.talebase.cloud.os.project.service;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.admin.dto.DSubordinate;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-2.
 */
@Service
public class AdminService {

    final static String SERVICE_NAME = "ms-admin";

    @Autowired
    private MsInvoker msInvoker;

    public List<TAdmin> findAdmins(List<String> accounts){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/getAdminsByAccounts";
        ServiceResponse<List<TAdmin>> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), accounts), new ParameterizedTypeReference<ServiceResponse<List<TAdmin>>>(){});
        return response.getResponse();
    }

    /**
     * 获取下级管理员
     */
    public List<DSubordinate> getSubordinate(Integer companyId, String orgCode) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/subordinate?companyId={companyId}&orgCode={orgCode}";
        ServiceResponse<List<DSubordinate>> response  = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<DSubordinate>>>(){}, companyId, orgCode);
        return  response.getResponse();
    }

    public TAdmin getAdminByAccount(String account){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/getByAccountAndCompanyId";
        DAdmin req = new DAdmin();
        req.setAccount(account);
        req.setCompanyId(ServiceHeaderUtil.getRequestHeader().getCompanyId());
        ServiceResponse<TAdmin> response  = msInvoker.post(servicePath, new ServiceRequest<DAdmin>(ServiceHeaderUtil.getRequestHeader(), req), new ParameterizedTypeReference<ServiceResponse<TAdmin>>(){});
        return  response.getResponse();
    }
}
