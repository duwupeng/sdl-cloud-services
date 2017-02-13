package com.talebase.cloud.os.consumption.service;

import com.google.common.reflect.TypeToken;
import com.talebase.cloud.base.ms.admin.dto.DGroupAndRole;
import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.consume.dto.*;
import com.talebase.cloud.base.ms.project.dto.DProjectSelect;
import com.talebase.cloud.base.ms.project.dto.DUseStatics;
import com.talebase.cloud.base.ms.project.dto.DUseStaticsQueryReq;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.consumption.dao.MsInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by suntree.xu on 2016-12-8.
 */
@Service
public class ConsumeService{

    final static String SERVICE_NAME = "ms-consumption";
    final static String PROSERVICE_NAME = "ms-project";

   // final static String SERVICE_NAME = "localhost:28005";

    @Autowired
    MsInvoker msInvoker;

    public List<DAccountComsumeDisplay> getDAccountComsumeDisplayList(DAccountCondition reqcondition) {
        List<DAccountComsumeDisplay> allResultList = new ArrayList<DAccountComsumeDisplay>();
        List<DAccountConsumeResult> consumeResultList = new ArrayList<DAccountConsumeResult>();
        String servicePath = "http://" + SERVICE_NAME + "/consume/getConsumesTotal";
        String proPath = "http://" + PROSERVICE_NAME + "/task/use/query";
        ServiceRequest<DAccountCondition> request = new ServiceRequest<DAccountCondition>();
        request.setRequest(reqcondition);
        request.setRequestHeader(ServiceHeaderUtil.getRequestHeader());
        ServiceResponse<List<DAccountConsumeResult>> response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<List<DAccountConsumeResult>>>(){});
        consumeResultList = response.getResponse();
        //根据消费结果集查找项目使用人数结果集然后拼装
        ServiceRequest<List<DUseStaticsQueryReq>> staticreq = new ServiceRequest<List<DUseStaticsQueryReq>>();
        staticreq.setRequestHeader(ServiceHeaderUtil.getRequestHeader());
        List<DUseStaticsQueryReq> reqList = new  ArrayList<DUseStaticsQueryReq>();
        for(DAccountConsumeResult result:consumeResultList){
            DUseStaticsQueryReq dUseStaticsQueryReq = new DUseStaticsQueryReq();
            dUseStaticsQueryReq.setAccount(result.getAccount());
            dUseStaticsQueryReq.setProjectId(result.getProjectId());
            dUseStaticsQueryReq.setTaskId(result.getTaskId());
            reqList.add(dUseStaticsQueryReq);
        }
        staticreq.setRequest(reqList);
        ServiceResponse<List<DUseStatics>>  proResponse =  msInvoker.post(proPath,staticreq, new ParameterizedTypeReference<ServiceResponse<List<DUseStatics>>>(){});
        List<DUseStatics> proList = proResponse.getResponse();
        for(DAccountConsumeResult result:consumeResultList){
            DAccountComsumeDisplay allresult = new DAccountComsumeDisplay();
            allresult.setAccount(result.getAccount());
            allresult.setPointUsed(result.getPointUsed());
            allresult.setSmsUsed(result.getSmsUsed());
            for(DUseStatics dUseStatics:proList){
                if(dUseStatics.getAccount().equals(result.getAccount())&&dUseStatics.getProjectId().equals(result.getProjectId())&&dUseStatics.getTaskId().equals(result.getTaskId())){
                    allresult.setUsedCount(dUseStatics.getInNum());
                    allresult.setDredgeCount(dUseStatics.getPreNum());
                    allresult.setProjectName(dUseStatics.getProjectName());
                    allresult.setTaskName(dUseStatics.getTaskName());
                }
            }
            allResultList.add(allresult);
        }

        return allResultList;
    }

    public List<DAccountPayResult> getDAccountPayResultList(ServiceRequest serviceRequest){
        String servicePath = "http://" + SERVICE_NAME + "/consume/getPaysTotal";
        ServiceResponse<List<DAccountPayResult>> response= msInvoker.post(servicePath,serviceRequest,new ParameterizedTypeReference<ServiceResponse<List<DAccountPayResult>>>(){});
        return response.getResponse();
    }

    public PageResponse<DAccountComsumeDisplay> getDAccountComsumeDisplayPage(DAccountCondition reqcondition, PageRequest request) {
        List<DAccountComsumeDisplay> allResultList = new ArrayList<DAccountComsumeDisplay>();
        List<DAccountConsumeResult> consumeResultList = new ArrayList<DAccountConsumeResult>();
        String servicePath = "http://" + SERVICE_NAME + "/consume/getConsumesPage";
        String proPath = "http://" + PROSERVICE_NAME + "/task/use/query";
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setRequest(reqcondition);
        serviceRequest.setRequestHeader(ServiceHeaderUtil.getRequestHeader());
        serviceRequest.setPageReq(request);
        ServiceResponse<PageResponse> response = msInvoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<PageResponse>>(){});
        PageResponse<DAccountComsumeDisplay> pageResponse =response.getResponse();
                //ServiceResponse<List<DAccountConsumeResult>> response = msInvoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<List<DAccountConsumeResult>>>(){});
        //consumeResultList = response.getResponse().getResults();
        List<LinkedHashMap> l = response.getResponse().getResults();
        for(int i=0;i<l.size();i++){
            DAccountConsumeResult dAccountConsumeResult = new DAccountConsumeResult();
            dAccountConsumeResult.setAccount(l.get(i).get("account").toString());
            dAccountConsumeResult.setPointUsed(Integer.parseInt(l.get(i).get("pointUsed").toString()));
            dAccountConsumeResult.setProjectId(Integer.parseInt(l.get(i).get("projectId").toString()));
            dAccountConsumeResult.setSmsUsed(Integer.parseInt(l.get(i).get("smsUsed").toString()));
            dAccountConsumeResult.setTaskId(Integer.parseInt(l.get(i).get("taskId").toString()));
            consumeResultList.add(dAccountConsumeResult);
        }
        //根据消费结果集查找项目使用人数结果集然后拼装。。。
        ServiceRequest<List<DUseStaticsQueryReq>> staticreq = new ServiceRequest<List<DUseStaticsQueryReq>>();
        List<DUseStaticsQueryReq> reqList = new  ArrayList<DUseStaticsQueryReq>();
        staticreq.setRequestHeader(ServiceHeaderUtil.getRequestHeader());
        for(DAccountConsumeResult result:consumeResultList){
            DUseStaticsQueryReq dUseStaticsQueryReq = new DUseStaticsQueryReq();
            dUseStaticsQueryReq.setAccount(result.getAccount());
            dUseStaticsQueryReq.setProjectId(result.getProjectId());
            dUseStaticsQueryReq.setTaskId(result.getTaskId());
            reqList.add(dUseStaticsQueryReq);
        }
        staticreq.setRequest(reqList);
        ServiceResponse<List<DUseStatics>>  proResponse =  msInvoker.post(proPath,staticreq, new ParameterizedTypeReference<ServiceResponse<List<DUseStatics>>>(){});
        List<DUseStatics> proList = proResponse.getResponse();
        for(DAccountConsumeResult result:consumeResultList){
            DAccountComsumeDisplay allresult = new DAccountComsumeDisplay();
            allresult.setAccount(result.getAccount());
            allresult.setPointUsed(result.getPointUsed());
            allresult.setSmsUsed(result.getSmsUsed());
            for(DUseStatics dUseStatics:proList){
                if(dUseStatics.getAccount().equals(result.getAccount())&&dUseStatics.getProjectId().equals(result.getProjectId())&&dUseStatics.getTaskId().equals(result.getTaskId())){
                    allresult.setUsedCount(dUseStatics.getInNum());
                    allresult.setDredgeCount(dUseStatics.getPreNum());
                    allresult.setProjectName(dUseStatics.getProjectName());
                    allresult.setTaskName(dUseStatics.getTaskName());
                }
            }
            allResultList.add(allresult);
        }

        pageResponse.setResults(allResultList);
        return  pageResponse;
    }

    public PageResponse<DAccountPayResultDisplay> queryPays(PageRequest request,DAccountCondition dAccountCondition) {
        ServiceRequest serviceRequest = new ServiceRequest(ServiceHeaderUtil.getRequestHeader());
        serviceRequest.setPageReq(request);
        String servicePath = "http://" + SERVICE_NAME + "/consume/getPays/"+dAccountCondition.getAccount();
        ServiceResponse<PageResponse> response =  msInvoker.post(servicePath,serviceRequest,new ParameterizedTypeReference<ServiceResponse<PageResponse>>(){});

        List<LinkedHashMap> l = response.getResponse().getResults();

        /*List<DAccountPayResult> list = new ArrayList<DAccountPayResult>();

        for(Object o : response.getResponse().getResults()){
            list.add(GsonUtil.fromJson(GsonUtil.toJson(o), new TypeToken<DAccountPayResult>(){}.getType()));
        }*/

//        List<DAccountPayResult> list =
        List<DAccountPayResult> tlist = new ArrayList<DAccountPayResult>();

        for(int i=0;i<l.size();i++){
            DAccountPayResult tdAccountPayResult = new DAccountPayResult();
            tdAccountPayResult.setAccount(l.get(i).get("account").toString());
            tdAccountPayResult.setModifiedDate(new Date(Long.parseLong(l.get(i).get("modifiedDate").toString())));
            tdAccountPayResult.setPointVar(Integer.parseInt(l.get(i).get("pointVar").toString()));
            tdAccountPayResult.setSmsVar(Integer.parseInt(l.get(i).get("smsVar").toString()));
            tlist.add(tdAccountPayResult);
        }
         List<DAccountPayResultDisplay> resultDisplays = new ArrayList<DAccountPayResultDisplay>();
        PageResponse<DAccountPayResultDisplay> pageResponse = response.getResponse();
        for(DAccountPayResult dAccountPayResult:tlist){
            DAccountPayResultDisplay dAccountPayResultDisplay = new DAccountPayResultDisplay();
            dAccountPayResultDisplay.setAccount(dAccountPayResult.getAccount());
            dAccountPayResultDisplay.setModifiedDate(dAccountPayResult.getModifiedDate());
            if(dAccountPayResult.getPointVar()==0) {
                dAccountPayResultDisplay.setType("短信");
                dAccountPayResultDisplay.setVal(dAccountPayResult.getSmsVar());
            }else if(dAccountPayResult.getSmsVar()==0){
                dAccountPayResultDisplay.setType("T币数");
                dAccountPayResultDisplay.setVal(dAccountPayResult.getPointVar());
            }
            resultDisplays.add(dAccountPayResultDisplay);
        }
        pageResponse.setResults(resultDisplays);
        return pageResponse;
    }

    public TAccount queryAccount(int companyId) {
        TAccount tAccount = new TAccount();
        String servicePath = "http://" + SERVICE_NAME + "/consume/queryAccount/"+companyId;
        ServiceResponse<TAccount> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<TAccount>>(){});
        tAccount = response.getResponse();
        return tAccount;
    }

    public ServiceResponse opoperateConsume(TAccountLine tAccountLine) {
        ServiceRequest serviceRequest = new ServiceRequest();
        //serviceRequest.setRequestHeader(ServiceHeaderUtil.getRequestHeader());
        serviceRequest.setRequest(tAccountLine);
        String servicePath = "http://" + SERVICE_NAME + "/consume/operate";
        ServiceResponse response =msInvoker.put(servicePath,serviceRequest,new ParameterizedTypeReference<ServiceResponse>(){});
        return response;
    }
    final static String SERVICE_NAME_PROJECT = "ms-project";
    /**
     * 获取分制和角色
     * @param request
     * @return
     */
    public ServiceResponse<List<DProjectSelect>> getGroupAndRoleForSelect(ServiceRequest request) {
        String servicePath = "http://" + SERVICE_NAME_PROJECT + "/project/select";
        ServiceResponse<List<DProjectSelect>> response = msInvoker.post(servicePath,request,new ParameterizedTypeReference<ServiceResponse<List<DProjectSelect>>>(){});
        return  response;
    }

}
