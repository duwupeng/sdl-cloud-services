package com.talebase.cloud.ms.examer.test.controller;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecord;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecordQueryReq;
import com.talebase.cloud.base.ms.examer.dto.DUserShowFieldReq;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserFieldControllerTest {

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
    public void test_查询项目任务有定义字段() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/fields/{companyId}?projectId={projectId}&taskId={taskId}";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<TUserShowField>> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<TUserShowField>>>(){}, 1, 1, 1);

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0 && response.getResponse().size() > 0;
    }

    @Test
    public void test_查询项目任务没定义返回全局字段() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/fields/{companyId}";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<TUserShowField>> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<TUserShowField>>>(){}, 1);

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0 && response.getResponse().size() > 0;
    }

    @Test
    public void test_查询考生信息字段() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/getFields/";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DUserShowFieldReq request = new DUserShowFieldReq();
        request.setCompanyId(1);
        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(1);
        request.setProjectIds(projectIds);
        List<Integer> taskIds = new ArrayList<>();
        taskIds.add(1);
        request.setTaskIds(taskIds);
        ServiceResponse<List<TUserShowField>> response = invoker.post(servicePath,request, new ParameterizedTypeReference<ServiceResponse<List<TUserShowField>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0 && response.getResponse().size() > 0;
    }
}
