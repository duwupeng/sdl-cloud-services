package com.talebase.cloud.os.admin.service;

import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.base.ms.admin.dto.DGroup;
import com.talebase.cloud.base.ms.admin.dto.DGroupCreateReq;
import com.talebase.cloud.common.MsInvoker;
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
public class GroupService {

    final static String SERVICE_NAME = "ms-admin";

    @Autowired
    private MsInvoker msInvoker;

    public ServiceResponse updateGroupName(Integer groupId, String groupName){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/group/name/" + groupId;
        ServiceResponse response = msInvoker.put(servicePath, new ServiceRequest<String>(ServiceHeaderUtil.getRequestHeader(),  groupName), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse deleteGroups(List<Integer> groupIds){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/groups/delete";
        ServiceResponse response = msInvoker.delete(servicePath, new ServiceRequest<List<Integer>>(ServiceHeaderUtil.getRequestHeader(),  groupIds), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse addGroup(DGroupCreateReq createReq){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/group";
        ServiceResponse response = msInvoker.post(servicePath, new ServiceRequest<DGroupCreateReq>(ServiceHeaderUtil.getRequestHeader(), createReq), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse<List<DGroup>> findByCompany(Integer companyId){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/groups/query";
        ServiceResponse<List<DGroup>> response = msInvoker.post(servicePath, new ServiceRequest<Integer>(ServiceHeaderUtil.getRequestHeader(), companyId), new ParameterizedTypeReference<ServiceResponse<List<DGroup>>>(){});
        return response;
    }

    public TGroup getGroup(Integer groupId){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/group/{groupId}";
        ServiceResponse<TGroup> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<TGroup>>(){}, groupId);
        return response.getResponse();
    }
}
