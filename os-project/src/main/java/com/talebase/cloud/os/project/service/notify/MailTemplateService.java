package com.talebase.cloud.os.project.service.notify;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-24.
 */

@Service
public class MailTemplateService {
    static final String SERVICE_NAME = "ms-notify";
    @Autowired
    MsInvoker invoker;

    public ServiceResponse<PageResponse<TNotifyTemplate>> getTemplates(PageRequest pageRequest) {
        String servicePath = "http://" + SERVICE_NAME + "/mailTemplates";
        ServiceResponse response = invoker.post(servicePath,new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(),pageRequest), new ParameterizedTypeReference<ServiceResponse<PageResponse<TNotifyTemplate>>>(){});
        return response;
    }
    public ServiceResponse<List<DNotifyTemplate>> get() {
        String servicePath = "http://" + SERVICE_NAME + "/mailTemplate/list";
        ServiceResponse response = invoker.post(servicePath,new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<List<DNotifyTemplate>>>(){});
        return response;
    }

    public ServiceResponse getTemplateByName(DNotifyTemplate dNotifyTemplate) {
        String servicePath = "http://" + SERVICE_NAME + "/mailTemplate/check";
        ServiceResponse<Integer> response = invoker.post(servicePath,new ServiceRequest<DNotifyTemplate>(ServiceHeaderUtil.getRequestHeader(),dNotifyTemplate), new ParameterizedTypeReference<ServiceResponse<Integer>>(){});
        if(response.getResponse() != 0){
            return new ServiceResponse(BizEnums.NOTIFY_UNIQUE_KEY,true);
        }
        return new ServiceResponse();
    }

    public ServiceResponse update(DNotifyTemplate dNotifyTemplate) {
        String servicePath = "http://" + SERVICE_NAME + "/mailTemplate";
        ServiceResponse response = invoker.put(servicePath, new ServiceRequest<DNotifyTemplate>(ServiceHeaderUtil.getRequestHeader(),dNotifyTemplate),new ParameterizedTypeReference<ServiceResponse<DNotifyTemplate>>(){});
        return new ServiceResponse();
    }

    public ServiceResponse updateStatus(Integer id,Boolean status) {
        String servicePath = "http://" + SERVICE_NAME + "/mailTemplate/status/" + id;
        ServiceResponse response = invoker.put(servicePath,new ServiceRequest<Boolean>(ServiceHeaderUtil.getRequestHeader(),  status), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return new ServiceResponse();
    }

    public ServiceResponse delete(String id) {
        String servicePath = "http://" + SERVICE_NAME + "/mailTemplate";
        ServiceResponse response = invoker.delete(servicePath, new ServiceRequest<List<Integer>>(ServiceHeaderUtil.getRequestHeader(), StringUtil.toIntListByComma(id)), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return new ServiceResponse();
    }

    public ServiceResponse add(DNotifyTemplate dNotifyTemplate) {
        String servicePath = "http://" + SERVICE_NAME + "/mailTemplate";
        ServiceResponse response = invoker.post(servicePath,new ServiceRequest<DNotifyTemplate>(ServiceHeaderUtil.getRequestHeader(),dNotifyTemplate), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return new ServiceResponse();
    }
}
