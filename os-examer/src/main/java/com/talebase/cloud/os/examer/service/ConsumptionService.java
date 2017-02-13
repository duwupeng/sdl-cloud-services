package com.talebase.cloud.os.examer.service;

import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.examer.dto.DEditExamerReq;
import com.talebase.cloud.base.ms.examer.dto.DUserExamPermission;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * Created by kanghong.zhao on 2017-1-14.
 */
@Service
public class ConsumptionService {

    final static String SERVICE_NAME = "ms-consumption";
    @Autowired
    MsInvoker msInvoker;

    /**
     * 消费
     * @param userExamPermission
     */
    public void cost(DUserExamPermission userExamPermission){

        String queryServicePath = "http://" + SERVICE_NAME + "/consume/queryAccount/" + ServiceHeaderUtil.getRequestHeader().getCompanyId();
        ServiceResponse<TAccount> response = msInvoker.get(queryServicePath, new ParameterizedTypeReference<ServiceResponse<TAccount>>(){});
        TAccount account = response.getResponse();
        if(account.getPointBalance() - account.getPeraccountValid() < 0 || account.getPointBalance() <= 0){
            throw new WrappedException(BizEnums.CostPointNotEnough);
        }

        String servicePath = "http://" + SERVICE_NAME + "/consume/operate";
        TAccountLine accountLine = new TAccountLine();
        accountLine.setProjectId(userExamPermission.getProjectId());
        accountLine.setCompanyId(ServiceHeaderUtil.getRequestHeader().getCompanyId());
        accountLine.setPointVar(account.getPeraccountValid());
        accountLine.setTaskId(userExamPermission.getTaskId());
        accountLine.setType(1);
        accountLine.setModifier(userExamPermission.getExamerAdmin() == null ? userExamPermission.getExamerAdmin() : userExamPermission.getProjectAdmin());
        accountLine.setRemark("[" + userExamPermission.getProjectName() + "]_[" + userExamPermission.getTaskName() + "]考生" + ServiceHeaderUtil.getRequestHeader().getCustomerName() + "参与考试");
        msInvoker.put(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), accountLine), new ParameterizedTypeReference<ServiceResponse<String>>(){});
    }

}
