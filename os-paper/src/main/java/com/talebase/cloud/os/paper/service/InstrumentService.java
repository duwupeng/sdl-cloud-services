package com.talebase.cloud.os.paper.service;

import com.talebase.cloud.base.ms.paper.dto.DInstruction;
import com.talebase.cloud.base.ms.paper.dto.DPage;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class InstrumentService {
    @Autowired
    MsInvoker msInvoker;

    final static String SERVICE_NAME = "ms-paper";


    /**
     * 添加卷页
     * @param req
     * @return
     */
    public ServiceResponse create(ServiceRequest<DInstruction> req) {
        String servicePath = "http://" + SERVICE_NAME + "/question/instruction";
        ServiceResponse<DInstruction> response = msInvoker.post(servicePath,req, new ParameterizedTypeReference<ServiceResponse<DInstruction>>(){});
        return  response;
    }
}
