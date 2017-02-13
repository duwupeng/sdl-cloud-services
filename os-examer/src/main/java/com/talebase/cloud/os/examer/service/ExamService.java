package com.talebase.cloud.os.examer.service;

import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.project.dto.DUseStatics;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daorong.li on 2016-12-6.
 */
@Service
public class ExamService {
    @Autowired
    MsInvoker msInvoker;

    final static String EXAM_SERVICE_NAME = "ms-examer";

    private static Logger log = LoggerFactory.getLogger(ExamService.class);

    /**
     * 添加可选信息
     * @param request
     * @return
     */
    public ServiceResponse<String> addExamByOptional(ServiceRequest<DUserShowField> request){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/add";
        ServiceResponse<String> response = msInvoker.post(servicePath,request,new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    /**
     * 删除自定义字段
     * @param request
     * @return
     */
    public ServiceResponse del( ServiceRequest<DUserShowField> request){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/del";
        ServiceResponse response = msInvoker.post(servicePath,request,new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    /**
     *
     * @param request
     * @return
     */
    public ServiceResponse saveGlobalAll(ServiceRequest<DUserShowField> request){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/saveGlobalAll";
        ServiceResponse response = msInvoker.post(servicePath,request,new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }



    /**
     * 分页查询账号导入日志
     * @param queryReq 查询条件
     * @param pageRequest 分页条件
     * @return
     */
    public ServiceResponse<PageResponse<DUserImportRecord>> queryImportLog(DUserImportRecordQueryReq queryReq, PageRequest pageRequest) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/importLog/query";
        ServiceResponse<PageResponse<DUserImportRecord>> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), queryReq, pageRequest),
                new ParameterizedTypeReference<ServiceResponse<PageResponse<DUserImportRecord>>>(){});
        return  response;
    }

    /**
     * 查询错误日志
     */
    public DTUserImportLogEx queryImportLog(String batchNo) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/failLog/export";
        ServiceResponse<DTUserImportLogEx> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), batchNo), new ParameterizedTypeReference<ServiceResponse<DTUserImportLogEx>>(){});
        return response.getResponse();
    }

    /**
     * 获取全局账号设置列表
     * @param companyId
     * @return
     */
    public ServiceResponse<DUserShowFieldResponseList> getGlobalExamers(Integer companyId){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examers/"+companyId;
        ServiceResponse<DUserShowFieldResponseList> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<DUserShowFieldResponseList>>(){});
        return response;
    }

    public List<TUserShowField> getUserFields(Integer companyId, Integer projectId, Integer taskId){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/fields/{companyId}";

        if(projectId != null && taskId != null)
            servicePath = servicePath + "?projectId=" + projectId + "&taskId=" + taskId;

        ServiceResponse<List<TUserShowField>> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<TUserShowField>>>(){}, companyId);
        return response.getResponse();
    }

    public List<String> importData(Integer projectId, Integer taskId, List<String> header, List<List<String>> details, ServiceHeader serviceHeader) throws Exception {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/import";
        DImportReq req = new DImportReq(projectId, taskId, header, details);
        ServiceRequest serviceRequest = new ServiceRequest(serviceHeader, req);
        ServiceResponse<List<String>> response = msInvoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<List<String>>>(){});

        if(response.getCode() != 0)
            log.error("导入数据失败");

        return response.getResponse();
    }

    public DCreateExamersResp createExamers(DCreateExamersReq req){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examers";
        ServiceRequest serviceRequest = new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), req);
        ServiceResponse<DCreateExamersResp> response = msInvoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<DCreateExamersResp>>(){});

        return response.getResponse();
    }


}
