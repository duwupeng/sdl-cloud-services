package com.talebase.cloud.ms.project.controller;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.notify.dto.DTaskFinishType;
import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.base.ms.project.enums.TTaskStatus;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskControllerTest {

    private int port = 28005;
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
    public void test_创建任务() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/tasks/25";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        List<DProjectTasksUpdateReq> reqs = find任务(3);
        ServiceResponse response = invoker.put(servicePath, new ServiceRequest(requestHeader, reqs), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_更新任务() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/tasks/25";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        List<DProjectTasksUpdateReq> reqs = find任务(3);
        reqs.get(0).setId(15);
        reqs.get(0).getExaminers().clear();
        reqs.get(0).setFinishType(DTaskFinishType.ExamTimeType);
        reqs.get(1).setId(16);

        System.out.println(GsonUtil.toJson(new ServiceRequest(requestHeader, reqs)));

        ServiceResponse response = invoker.put(servicePath, new ServiceRequest(requestHeader, reqs), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    private List<DProjectTasksUpdateReq> find任务(int size){
        List<DProjectTasksUpdateReq> list = new ArrayList<>();
        for(int i = 0; i < size; i++){
            list.add(get任务("任务" + i));
        }
        return list;
    }

    private DProjectTasksUpdateReq get任务(String taskName){
        DProjectTasksUpdateReq dto = new DProjectTasksUpdateReq();
        dto.setName(taskName);
//        dto.setId(1);
        dto.setPaperId(1);
        dto.setPaperUnicode("12345678");
        dto.setPaperVersion(BigDecimal.valueOf(1));
        dto.setNeedMarkingNum(1);
        dto.setDelayLimitTime(15);
        dto.setEndDateStr("2016-12-01 12:00:00");

        List<TTaskExaminer> examiners = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            TTaskExaminer examiner = new TTaskExaminer();
            examiner.setName("user" + i);
            examiner.setExaminer("examiner" + i);
            examiners.add(examiner);
        }

        dto.setExaminers(examiners);
        dto.setExamTime(60);
        dto.setFinishType(1);
        dto.setLatestStartDateStr("2016-12-01 12:30:00");
        dto.setPageChangeLimit(3);
        dto.setStartDateStr("2016-12-01 10:00:00");

        return dto;
    }

//    @Test
//    public void test_修改() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/project/" + 25;
//
//        MsInvoker invoker = new MsInvoker();
//        invoker.setRestTemplate(restTemplate);
//
//        DProjectEditReq createReq = get扫码项目("有码项目");
//
//        createReq.setScanEnable(false);
//        createReq.getProjectAdmins().clear();
//
//        ServiceResponse response = invoker.put(servicePath, new ServiceRequest<DProjectEditReq>(requestHeader, createReq), new ParameterizedTypeReference<ServiceResponse<String>>(){});
//
//        assert response != null && response.getCode() == 0;
//    }
//
//    @Test
//    public void test_修改无码项目() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/project/" + 24;
//
//        MsInvoker invoker = new MsInvoker();
//        invoker.setRestTemplate(restTemplate);
//
//        DProjectEditReq createReq = get扫码项目("无码项目");
//
//        ServiceResponse response = invoker.put(servicePath, new ServiceRequest<DProjectEditReq>(requestHeader, createReq), new ParameterizedTypeReference<ServiceResponse<String>>(){});
//
//        assert response != null && response.getCode() == 0;
//    }

//    @Test
//    public void test_get任务inEdit() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/project/edit/" + 25;
//
//        MsInvoker invoker = new MsInvoker();
//        invoker.setRestTemplate(restTemplate);
//
//        ServiceResponse<DProjectInEdit> response = invoker.post(servicePath, new ServiceRequest<DProjectEditReq>(requestHeader), new ParameterizedTypeReference<ServiceResponse<DProjectInEdit>>(){});
//
//        System.out.println(new Gson().toJson(response));
//
//        assert response != null && response.getCode() == 0;
//    }

    @Test
    public void test_禁用任务() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/task/status/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.put(servicePath, new ServiceRequest(requestHeader, TTaskStatus.DISABLE.getValue()), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_启用任务() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/task/status/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.put(servicePath, new ServiceRequest(requestHeader, TTaskStatus.ENABLE.getValue()), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_删除任务() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/task/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.delete(servicePath, new ServiceRequest(requestHeader), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_任务编辑页面ByProject() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/project/tasks/" + 25;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.post(servicePath, new ServiceRequest(requestHeader), new ParameterizedTypeReference<ServiceResponse<List<DTaskInEdit>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_根据任务id查询评卷人() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/task/examiners/{taskId}";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<TTaskExaminer>> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<TTaskExaminer>>>(){}, 16);

        System.out.println(GsonUtil.toJson(response));
        assert response != null && response.getCode() == 0 && response.getResponse().size() > 0;
    }

    @Test
    public void test_根据评卷人查询任务分页列表() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/tasks/examiner/{companyId}/{operatorName}?pageIndex={pageIndex}&limit={limit}";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<PageResponse<DTaskInScore>> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<PageResponse<DTaskInScore>>>(){}, 1, "zhaokh", 1, 15);

        System.out.println(GsonUtil.toJson(response));
        assert response != null && response.getCode() == 0 && response.getResponse().getTotal() > 0;
    }

    @Test
    public void test_根据评卷人查询评卷情况统计() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/tasks/mark/{companyId}/{operatorName}";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<DTaskMarked> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DTaskMarked>>(){}, 1, "zhaokh");

        System.out.println(GsonUtil.toJson(response));
        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_根据考官获取评卷任务() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/task/check/examiner";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceRequest serviceRequest = new ServiceRequest(requestHeader, 16);
        System.out.println(GsonUtil.toJson(serviceRequest));
        ServiceResponse<TTask> response = invoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<TTask>>(){});

        System.out.println(GsonUtil.toJson(response));
        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_查询使用统计() {
        String servicePath = "http://192.168.1.31:" + port + servicePathPre + "/task/use/query";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

//        DUseStaticsQueryReq req = new DUseStaticsQueryReq("zhaokh", 1, 1);
        DUseStaticsQueryReq req = new DUseStaticsQueryReq();
        ServiceRequest serviceRequest = new ServiceRequest(requestHeader, Arrays.asList(req));
        System.out.println(GsonUtil.toJson(serviceRequest));

        ServiceResponse<List<DUseStatics>> response = invoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<List<DUseStatics>>>(){});

        System.out.println(GsonUtil.toJson(response));
        assert response != null && response.getCode() == 0;
    }

}
