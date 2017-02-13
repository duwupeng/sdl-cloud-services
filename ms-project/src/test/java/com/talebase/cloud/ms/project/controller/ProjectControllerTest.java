package com.talebase.cloud.ms.project.controller;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.project.domain.TProjectAdmin;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.base.ms.project.enums.TProjectStatus;
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
 * Created by kanghong.zhao on 2016-12-2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectControllerTest {

    private int port = 28005;
    private String servicePathPre = "";
    private RestTemplate restTemplate = new RestTemplate();

    private ServiceHeader requestHeader;

    @Before
    public void before(){
//        INSERT INTO `admindb`.`t_company` (`id`, `name`, `short_name`, `address`, `logo`, `domain`, `status`, `web_site`, `post_code`) VALUES ('1', '测试公司', '测试', '1', '1', '1', '1', '1', '1');
//        INSERT INTO `admindb`.`t_group` (`id`, `parent_id`, `name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `org_code`, `company_id`) VALUES ('1', '0', '第一组', '1', '2016-01-01', '1', '2016-01-01', '1', '1_', '1');
//        INSERT INTO `admindb`.`t_group` (`id`, `parent_id`, `name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `org_code`, `company_id`) VALUES ('2', '1', '第二组', '1', '2016-01-01', '1', '2016-01-01', '1', '1_2_', '1');
//        INSERT INTO `admindb`.`t_group` (`id`, `parent_id`, `name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `org_code`, `company_id`) VALUES ('3', '2', 'Team3', '1', '2016-01-01', '1', '2016-01-01', '1', '1_2_3_', '1');
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
    public void test_创建无扫码项目() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/project";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DProjectEditReq createReq = get无码项目("无码项目");

        ServiceResponse<Integer> response = invoker.post(servicePath, new ServiceRequest<DProjectEditReq>(requestHeader, createReq), new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

        assert response != null && response.getCode() == 0 && response.getResponse() > 0;
    }

    @Test
    public void test_创建扫码项目() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DProjectEditReq createReq = get扫码项目("扫码项目");

        System.out.println(GsonUtil.toJson(new ServiceRequest<DProjectEditReq>(requestHeader, createReq)));

        ServiceResponse<Integer> response = invoker.post(servicePath, new ServiceRequest<DProjectEditReq>(requestHeader, createReq), new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

        assert response != null && response.getCode() == 0 && response.getResponse() > 0;
    }

    private DProjectEditReq get扫码项目(String projectName){
        DProjectEditReq createReq = get无码项目(projectName);
        createReq.setScanEnable(true);
        createReq.setScanAccountPre("pre");
        createReq.setScanMax(100);
        createReq.setScanStartDateStr("2016-12-01 10:00:00");
        createReq.setScanEndDateStr("2016-12-01 12:00:00");
        return createReq;
    }

    private DProjectEditReq get无码项目(String projectName){
        DProjectEditReq createReq = new DProjectEditReq();
        createReq.setName(projectName);
        createReq.setDescription("描述1");
        createReq.setEndDateStr("2016-12-01 12:00:00");
        createReq.setStartDateStr("2016-12-01 10:00:00");

        createReq.setScanEnable(false);

        List<TProjectAdmin> admins = new ArrayList<>();
        for(int i = 1; i < 4; i++) {
            TProjectAdmin admin = new TProjectAdmin();
            admins.add(admin);
            admin.setAccount("account" + i);
            admin.setName("User" + i);
        }
        createReq.setProjectAdmins(admins);
        return createReq;
    }

    @Test
    public void test_修改扫码项目() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DProjectEditReq createReq = get扫码项目("有码项目");

        createReq.setScanEnable(false);
        createReq.getProjectAdmins().clear();

        ServiceResponse response = invoker.put(servicePath, new ServiceRequest<DProjectEditReq>(requestHeader, createReq), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_修改无码项目() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/" + 24;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DProjectEditReq createReq = get扫码项目("无码项目");

        ServiceResponse response = invoker.put(servicePath, new ServiceRequest<DProjectEditReq>(requestHeader, createReq), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_get项目inEdit() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/edit/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<DProjectInEdit> response = invoker.post(servicePath, new ServiceRequest<DProjectEditReq>(requestHeader), new ParameterizedTypeReference<ServiceResponse<DProjectInEdit>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_禁用项目() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/status/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.put(servicePath, new ServiceRequest(requestHeader, TProjectStatus.DISABLE.getValue()), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_启用项目() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/status/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.put(servicePath, new ServiceRequest(requestHeader, TProjectStatus.ENABLE.getValue()), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_删除项目() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.delete(servicePath, new ServiceRequest(requestHeader), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_项目列表() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/projects/query";

        DProjectQueryReq queryReq = new DProjectQueryReq();
//        queryReq.setProjectNameLike("有");
//        queryReq.setTaskNameLike("2");

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageIndex(1);
        pageRequest.setLimit(5);

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<PageResponse<DProject>> response = invoker.post(servicePath, new ServiceRequest(requestHeader, queryReq, pageRequest), new ParameterizedTypeReference<ServiceResponse<PageResponse<DProject>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_项目错误通知更新() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/errMsg/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        Integer addNum = 3;
        ServiceResponse response = invoker.put(servicePath, new ServiceRequest(requestHeader, addNum), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_项目错误通知清空() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/errMsg/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.delete(servicePath, new ServiceRequest(requestHeader), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_获取项目任务下拉框数据(){
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/select";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<DProjectSelect>> response = invoker.post(servicePath, new ServiceRequest(requestHeader), new ParameterizedTypeReference<ServiceResponse<List<DProjectSelect>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0 && response.getResponse().size() > 0;
    }

    @Test
    public void test_复制项目(){
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/copy";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DProjectCopyReq req = new DProjectCopyReq();
        req.setSourceProjectId(25);
        req.setName("复制项目3");
        ServiceRequest<DProjectCopyReq> serviceRequest = new ServiceRequest(requestHeader, req);

        System.out.println(GsonUtil.toJson(serviceRequest));

        ServiceResponse response = invoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 00;
    }

    @Test
    public void test_根据考生ID查找项目任务列表(){
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/byUserId";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);
        requestHeader.setCustomerId(3);
        ServiceRequest<DProjectCopyReq> serviceRequest = new ServiceRequest(requestHeader);

        ServiceResponse response = invoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<List<DExamTaskResponse>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 00;
    }

    @Test
    public void test_冗余管理员数据(){
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/group/admin";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        TAdmin admin = new TAdmin();
        admin.setAccount("zhao123");
        admin.setCompanyId(1);
        admin.setOrgCode("1_2_");
        ServiceRequest<TAdmin> serviceRequest = new ServiceRequest(requestHeader, admin);

        System.out.println(GsonUtil.toJson(serviceRequest));

        ServiceResponse response = invoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<String>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

}
