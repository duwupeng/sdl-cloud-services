package com.talebase.cloud.ms.examer.test.controller;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.examer.dto.DDataManagementRequest;
import com.talebase.cloud.base.ms.examer.dto.DDataManagementResponse;
import com.talebase.cloud.base.ms.examer.dto.DExamineeInTask;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kanghong.zhao on 2016-12-20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class DataManagementControllerTest {

    private int port = 28008;
    private String servicePathPre = "";
    private RestTemplate restTemplate = new RestTemplate();

    private ServiceHeader requestHeader;

    @Before
    public void before(){

        requestHeader = new ServiceHeader();
        requestHeader.operatorId = 1;
        requestHeader.operatorName = "zhaokh";
        requestHeader.callerFrom = CallerFrom.web;
        requestHeader.callerIP = "127.0.0.1";
        requestHeader.orgCode = "1_";
        requestHeader.companyId = 1;

        requestHeader.transactionId = "123456789";
    }

    @Test
    public void test_查询列表() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/dataManagement";

        ServiceRequest serviceRequest = new ServiceRequest();
        PageRequest pageRequest = new PageRequest();
        pageRequest.setLimit(10);
        pageRequest.setPageIndex(1);
        serviceRequest.setPageReq(pageRequest);
        serviceRequest.setRequestHeader(requestHeader);
        DDataManagementRequest dDataManagementRequest = new DDataManagementRequest();
        dDataManagementRequest.setProjectId(1);
        dDataManagementRequest.setTaskId(1);
        serviceRequest.setRequest(dDataManagementRequest);
        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<PageResponse<DDataManagementResponse>> response = invoker.post(servicePath,serviceRequest, new ParameterizedTypeReference<ServiceResponse<PageResponse<DDataManagementResponse>>>(){});

        System.out.println(GsonUtil.toJson(response));
        assert response != null && response.getCode() == 0;
    }


}
