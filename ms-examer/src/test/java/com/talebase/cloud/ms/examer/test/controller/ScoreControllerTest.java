package com.talebase.cloud.ms.examer.test.controller;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.examer.dto.DCreateExamersReq;
import com.talebase.cloud.base.ms.examer.dto.DCreateExamersResp;
import com.talebase.cloud.base.ms.examer.dto.DExamineeInTask;
import com.talebase.cloud.base.ms.examer.dto.DExamineeTaskDetail;
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
public class ScoreControllerTest {

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
    public void test_查询考生评卷情况分页列表() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/exam/task/{taskId}?pageIndex={pageIndex}&limit={limit}";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<PageResponse<DExamineeInTask>> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<PageResponse<DExamineeInTask>>>(){}, 1, 1, 50);

        System.out.println(GsonUtil.toJson(response));
        assert response != null && response.getCode() == 0;
    }


}
