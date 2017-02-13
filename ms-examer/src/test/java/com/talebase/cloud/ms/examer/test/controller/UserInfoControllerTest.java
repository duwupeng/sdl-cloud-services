package com.talebase.cloud.ms.examer.test.controller;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.ms.examer.util.UserUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserInfoControllerTest {

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
    public void test_根据数量创建多个账号() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/examers";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DCreateExamersReq req = new DCreateExamersReq();
        req.setAccountPre("test");
//        req.setSuffix("_sys");
        req.setAmount(10);
        req.setProjectId(1);
        req.setTaskId(1);

        ServiceResponse<DCreateExamersResp> response = invoker.post(servicePath, new ServiceRequest<DCreateExamersReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<DCreateExamersResp>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_自定义创建多个账号() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/examers";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DCreateExamersReq req = new DCreateExamersReq();
        req.setAccountPre("test");
//        req.setPassword("12345");
        req.setStartNum(165);
        req.setEndNum(170);
        req.setProjectId(1);
        req.setTaskId(1);

        ServiceResponse<DCreateExamersResp> response = invoker.post(servicePath, new ServiceRequest<DCreateExamersReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<DCreateExamersResp>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_自定义创建单个账号() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DEditExamerReq req = new DEditExamerReq();
        req.setProjectId(1);
        req.setTaskId(1);

        Map<String, String> map = new HashMap<>();
        map.put(UserUtil.ACCOUNT, "zhao1");
        map.put(UserUtil.PASSWORD, "123456");
        map.put(UserUtil.MOBILE, "13580406355");
        map.put(UserUtil.WORKYEARS, "7");
        map.put("xxx", "123456789");

        req.setMap(map);

        ServiceResponse<String> response = invoker.postHandleCodeSelf(servicePath, new ServiceRequest<DEditExamerReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        System.out.println(GsonUtil.toJson(response));

//        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_管理后台修改单个账号() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/" + 169;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DEditExamerReq req = new DEditExamerReq();
        req.setProjectId(1);
        req.setTaskId(1);

        Map<String, String> map = new HashMap<>();

        map.put(UserUtil.ACCOUNT, "zhao2");
        map.put(UserUtil.PASSWORD, "123456");
        map.put(UserUtil.MOBILE, "13580416355");
        map.put(UserUtil.WORKYEARS, "7");

        req.setMap(map);

        ServiceResponse<String> response = invoker.putHandleCodeSelf(servicePath, new ServiceRequest<DEditExamerReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        System.out.println(GsonUtil.toJson(response));

//        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_获取单个用户信息() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/getUserInfo";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DUserRequest dUserRequest = new DUserRequest();
        dUserRequest.setMobile("13538735545");
        ServiceResponse<TUserInfo> response = invoker.post(servicePath, dUserRequest, new ParameterizedTypeReference<ServiceResponse<TUserInfo>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_创建单个用户信息() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/createUserForPerfect";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DEditExamerReq dEditExamerReq = new DEditExamerReq();
        dEditExamerReq.setProjectId(1);
        dEditExamerReq.setTaskId(1);
        Map<String, String> map = new HashMap<>();
        map.put("account","testt");
        map.put("password","test1234");
        map.put("companyId","1");
        dEditExamerReq.setMap(map);
        ServiceRequest<DEditExamerReq> req = new ServiceRequest<DEditExamerReq>(requestHeader);
        req.setRequest(dEditExamerReq);
        ServiceResponse<Integer> response = invoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_修改单个用户信息() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/modifyUserForPerfect";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DEditExamerReq dEditExamerReq = new DEditExamerReq();
        dEditExamerReq.setProjectId(1);
        dEditExamerReq.setTaskId(1);
        Map<String, String> map = new HashMap<>();
        map.put("id","164");
        map.put("account","testt");
        map.put("companyId","1");
        map.put("name","testt");
        dEditExamerReq.setMap(map);
        ServiceRequest<DEditExamerReq> req = new ServiceRequest<DEditExamerReq>(requestHeader);
        req.setRequest(dEditExamerReq);
        ServiceResponse<String> response = invoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

}
