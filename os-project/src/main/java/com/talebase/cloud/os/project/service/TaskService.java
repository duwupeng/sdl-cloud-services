package com.talebase.cloud.os.project.service;

import com.talebase.cloud.base.ms.examer.dto.DDataManagementRequest;
import com.talebase.cloud.base.ms.examer.dto.DDataManagementResponse;
import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.base.ms.project.dto.DProjectTasksUpdateReq;
import com.talebase.cloud.base.ms.project.dto.DTTaskEx;
import com.talebase.cloud.base.ms.project.dto.DTaskInEdit;
import com.talebase.cloud.base.ms.project.dto.DTasksInEdit;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-3.
 */
@Service
public class TaskService {

    final static String SERVICE_NAME = "ms-project";

    final static String EXAMER_SERVICE_NAME = "ms-examer";

    @Autowired
    private MsInvoker msInvoker;

    public ServiceResponse<DTasksInEdit> findTaskInEdit(Integer projectId){
        String servicePath = "http://" + SERVICE_NAME + "/project/tasks/" + projectId;
        ServiceResponse<DTasksInEdit> response = msInvoker.post(servicePath, new ServiceRequest<Integer>(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<DTasksInEdit>>(){});
        return response;
    }

    public ServiceResponse updateTaskStatus(Integer taskId, Integer newStatus) {
        String servicePath = "http://" + SERVICE_NAME + "/task/status/" + taskId;
        ServiceResponse response = msInvoker.put(servicePath, new ServiceRequest<Integer>(ServiceHeaderUtil.getRequestHeader(), newStatus), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse deleteTask(Integer taskId){
        String servicePath = "http://" + SERVICE_NAME + "/task/" + taskId;
        ServiceResponse response = msInvoker.delete(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse updateTasks(Integer projectId, List<DProjectTasksUpdateReq> updateReqs){
        String servicePath = "http://" + SERVICE_NAME + "/project/tasks/" + projectId;
        ServiceResponse response = msInvoker.put(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), updateReqs), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public TTask getTask(Integer taskId){
        String servicePath = "http://" + SERVICE_NAME + "/task/{{taskId}";
        ServiceResponse<TTask> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<TTask>>(){}, taskId);
        return response.getResponse();
    }

    public ServiceResponse<DTTaskEx> findTaskExAndDataManagement(DDataManagementRequest dDataManagementRequest,PageRequest pageRequest){
        String servicePath = "http://" + SERVICE_NAME + "/task/findTaskExById/" + dDataManagementRequest.getTaskId();
        ServiceResponse<DTTaskEx> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DTTaskEx>>(){});
        if(response.getResponse() != null){
            servicePath = "http://" + EXAMER_SERVICE_NAME + "/examer/dataManagement";
            ServiceResponse<PageResponse<DDataManagementResponse>> pageResponseServiceResponse = msInvoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(),dDataManagementRequest,pageRequest),new ParameterizedTypeReference<ServiceResponse<PageResponse<DDataManagementResponse>>>(){});
            response.getResponse().setPageResponse(pageResponseServiceResponse.getResponse());
        }
        return response;
    }

    public void updatePaperVersion(TTask updateReq){
        String servicePath = "http://" + SERVICE_NAME + "/task/paper";
        msInvoker.put(servicePath, new ServiceRequest<TTask>(ServiceHeaderUtil.getRequestHeader(), updateReq), new ParameterizedTypeReference<ServiceResponse<String>>(){});
    }

}
