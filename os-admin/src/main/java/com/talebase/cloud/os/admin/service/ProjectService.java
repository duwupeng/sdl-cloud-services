package com.talebase.cloud.os.admin.service;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * Created by kanghong.zhao on 2017-1-4.
 */
@Service
public class ProjectService {

    final static String SERVICE_NAME = "ms-project";

    @Autowired
    private MsInvoker msInvoker;

    public ServiceResponse saveProjectGroupAdmin(TAdmin admin, ServiceHeader header){
        String servicePath = "http://" + SERVICE_NAME + "/project/group/admin";
        ServiceResponse response = msInvoker.post(servicePath, new ServiceRequest<TAdmin>(header,  admin), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }
}
