package com.talebase.cloud.os.admin.service.company;

import com.talebase.cloud.base.ms.admin.dto.DCompany;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * Created by daorong.li on 2016-11-28.
 */

@Service
public class UploadLogoService {

    @Autowired
    MsInvoker msInvoker;

    final static String SERVICE_NAME = "ms-company";

    public ServiceResponse<DCompany> updateLogo(DCompany dCompany) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceCompany/updateLogo";
        ServiceRequest<DCompany>  req = new ServiceRequest< DCompany>();
        req.setRequest(dCompany);
        ServiceResponse<DCompany> response = msInvoker.put(servicePath,req,new ParameterizedTypeReference<ServiceResponse<DCompany>>(){});
        return  response;
    }
}
