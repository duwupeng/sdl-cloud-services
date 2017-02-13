package com.talebase.cloud.os.project.service.notify;

import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-2.
 */
@Service
public class EmailService {

    static final String SERVICE_NAME = "ms-email";
    @Autowired
    MsInvoker invoker;

    public ServiceResponse<List<DEmailLog>> getEmails(ServiceRequest<DEmailLog> req ) {
        String servicePath = "http://" + SERVICE_NAME + "/emails";
        ServiceResponse<List<DEmailLog>>  response = invoker.post(servicePath,req, new ParameterizedTypeReference<ServiceResponse<List<DEmailLog>>>(){});
        return response;
    }
}
