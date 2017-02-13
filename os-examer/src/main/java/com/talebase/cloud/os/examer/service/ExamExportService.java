package com.talebase.cloud.os.examer.service;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.examer.domain.TScore;
import com.talebase.cloud.base.ms.examer.dto.DScoreJsonValue;
import com.talebase.cloud.base.ms.examer.dto.DTaskExamInfo;
import com.talebase.cloud.base.ms.examer.dto.DUserExamInfo;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by eric.du on 2016-12-12.
 */
@Service
public class ExamExportService {
    @Autowired
    MsInvoker msInvoker;
    final static String EXAM_SERVICE_NAME = "ms-examer";
    final static String PROJECT_SERVICE_NAME = "ms-project";
    /**
     * 获得考生列表
     * @param
     * @return
     */
    public ServiceResponse<List<DUserExamInfo>> findExamers(int taskId){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/examers/"+taskId;
        ServiceResponse<List<DUserExamInfo>> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<DUserExamInfo>>>(){});
        return response;
    }

    /**
     * 获得任务详情
     * @param
     * @return
     */
    public ServiceResponse<DTaskExamInfo> getTaskExamInfo(int taskId){
        String servicePath = "http://" + PROJECT_SERVICE_NAME + "/taskExam";
        ServiceResponse<DTaskExamInfo> response = msInvoker.post(servicePath,new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), taskId), new ParameterizedTypeReference<ServiceResponse<DTaskExamInfo>>(){});
        return response;
    }


    /**
     * 获得考生的主观题分数列表
     * @param
     * @return
     */
    public  Map<String,Map<String,double[]>> getScoreByTaskId(int taskId){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/getScoreByTaskId/"+taskId;
        ServiceResponse<String> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<String>>(){});
        String result = response.getResponse();
        Map<String,Map<String,double[]>> re = GsonUtil.fromJson(result,new TypeToken<Map<String,Map<String,double[]>>>() {}.getType());
        return re;
    }
}
