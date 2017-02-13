package com.talebase.cloud.os.admin.service;

import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.admin.dto.DUploadFileData;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by daorong.li on 2016-11-28.
 */
@Service
public class ExportService {
    @Autowired
    MsInvoker msInvoker;

    final static String SERVICE_NAME = "ms-admin";

    public List<DUploadFileData> getExportData(ServiceRequest<DAdmin> req){
        String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/getExportData";
        ServiceResponse<List<DUploadFileData>> response  =
                msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<List<DUploadFileData>>>(){});
        return response.getResponse();
    }

}
