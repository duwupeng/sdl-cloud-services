package com.talebase.cloud.os.examer.service;

import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.dto.DEditExamerReq;
import com.talebase.cloud.base.ms.examer.dto.DUpdatePassword;
import com.talebase.cloud.base.ms.examer.dto.DUserRequest;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * Created by zhangchunlin on 2016-12-16.
 */
@Service
public class UserInfoService {
    @Autowired
    MsInvoker msInvoker;

    final static String EXAM_SERVICE_NAME = "ms-examer";

    private static Logger log = LoggerFactory.getLogger(UserInfoService.class);

    /**
     * 修改用户资料（app）
     * @param request
     * @return
     */
    public ServiceResponse<String> modifyUserForPerfect(ServiceRequest<DEditExamerReq> request){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/modifyUserForPerfect";
        ServiceResponse<String> response = msInvoker.post(servicePath,request,new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse<String> updateUserPassword(DUpdatePassword dUpdatePassword){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/user/updateUserPassword";
        ServiceResponse<String> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(),dUpdatePassword), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }
}
