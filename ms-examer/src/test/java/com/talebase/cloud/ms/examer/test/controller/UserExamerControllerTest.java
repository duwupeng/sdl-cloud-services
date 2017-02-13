package com.talebase.cloud.ms.examer.test.controller;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.dto.DCreateExamersReq;
import com.talebase.cloud.base.ms.examer.dto.DCreateExamersResp;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by zhangchunlin on 2016-12-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserExamerControllerTest {

    private int port = 28008;
    private String servicePathPre = "";
    private RestTemplate restTemplate = new RestTemplate();

    private ServiceHeader requestHeader;

    @Before
    public void before(){

        requestHeader = new ServiceHeader();
        requestHeader.operatorId = 1;
        requestHeader.operatorName = "zhaokh";
//        requestHeader.operatorName = "account1";
        requestHeader.callerFrom = CallerFrom.web;
        requestHeader.callerIP = "127.0.0.1";
        requestHeader.orgCode = "1_";
//        requestHeader.orgCode = "1_2_3_4_";
        requestHeader.companyId = 1;

        requestHeader.transactionId = "123456789";
    }

    @Test
    public void getUserExam() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/getUserExam";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<TUserExam>> response = invoker.post(servicePath, 3, new ParameterizedTypeReference<ServiceResponse<List<TUserExam>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

}
