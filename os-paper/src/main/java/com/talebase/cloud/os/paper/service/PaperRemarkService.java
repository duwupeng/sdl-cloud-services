package com.talebase.cloud.os.paper.service;

import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.paper.dto.DPage;
import com.talebase.cloud.base.ms.paper.dto.DPaperRemark;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class PaperRemarkService {
    @Autowired
    MsInvoker msInvoker;

    final static String SERVICE_NAME = "ms-paper";


    /**
     * 添加卷结束语
     * @param req
     * @return
     */
    public ServiceResponse getPaperRemarks(ServiceRequest<DPaperRemark> req) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/remark";
        ServiceResponse<DPaperRemark> response = msInvoker.post(servicePath,req, new ParameterizedTypeReference<ServiceResponse<DPaperRemark>>(){});
        return  response;
    }

}
