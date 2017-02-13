package com.talebase.cloud;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.dto.DOption;
import com.talebase.cloud.base.ms.paper.dto.DPaper;
import com.talebase.cloud.base.ms.paper.dto.DPaperQuery;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by bin.yang on 2016-12-9.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class paperTest {
    private int port = 28007;

    private RestTemplate restTemplate = new RestTemplate();
    private ServiceHeader requestHeader;
    @Before
    public void before() {
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
    public void getList() {
        String servicePath = "http://localhost:" + port + "/question/papers";
        DPaperQuery dPaperQuery = new DPaperQuery();
//        dPaperQuery.setName("中级考试1");
        PageRequest pageRequest = new PageRequest();
        pageRequest.setLimit(10);
        pageRequest.setPageIndex(0);
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("zhaokh");

        ServiceRequest<List<DPaperQuery>> req = new ServiceRequest(serviceHeader, dPaperQuery, pageRequest);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<PageResponse<DPaper>>>() {
        });
        System.out.println("通过了哟");
    }

    @Test
    public void getListByIds() {
        String servicePath = "http://localhost:" + port + "/question/papers/byIds";
//        dPaperQuery.setName("中级考试1");
        List<Integer> ids = new ArrayList<>();
        ids.add(15);
        ids.add(17);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setLimit(10);
        pageRequest.setPageIndex(0);
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("zhaokh");

        ServiceRequest<List<DPaperQuery>> req = new ServiceRequest(serviceHeader, ids, pageRequest);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<PageResponse<DPaper>>>() {
        });
        System.out.println("通过了哟");
    }

    @Test
    public void updateStatus() {
        String servicePath = "http://localhost:" + port + "/question/paper/1";
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");

        Boolean status = true;

        ServiceRequest<List<DOption>> req = new ServiceRequest(serviceHeader, status);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.put(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>() {});
        System.out.println("通过了哟");
    }

    @Test
    public void delete() {
        String servicePath = "http://localhost:" + port + "/question/paper/1";
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("zhaokh");

        ServiceRequest<List<DOption>> req = new ServiceRequest(serviceHeader);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.delete(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        System.out.println("通过了哟");
    }

    @Test
    public void test_获取单个试卷基础信息(){
        String servicePath = "http://localhost:" + port + "/question/paper/simple/{paperId}";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceResponse<TPaper> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<TPaper>>() {}, 31);
        System.out.println(GsonUtil.toJson(response));

        assert response.getResponse() != null;
    }

    @Test
    public void test_根据id获取多个试卷基础信息(){
        String servicePath = "http://localhost:" + port + "/question/papers/query";
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceRequest serviceRequest = new ServiceRequest<>(requestHeader, Arrays.asList(31, 33));
        System.out.println(GsonUtil.toJson(serviceRequest));

        ServiceResponse<List<TPaper>> response = msInvoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<List<TPaper>>>(){});
        System.out.println(GsonUtil.toJson(response));

        assert response.getResponse() != null;
    }

    @Test
    public void test_根据多个试卷编号获取多个试卷最新版本(){
        String servicePath = "http://localhost:" + port + "/question/paper/byUnicode";
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceRequest serviceRequest = new ServiceRequest<>(requestHeader, Arrays.asList("P1215204600583-header-1", "P1219105151656-header-1"));
        System.out.println(GsonUtil.toJson(serviceRequest));

        ServiceResponse<List<TPaper>> response = msInvoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<List<TPaper>>>(){});
        System.out.println(GsonUtil.toJson(response));

        assert response.getResponse() != null;
    }

    public static void main(String[] args) {
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
      Collections.shuffle(list) ;

    }
}
