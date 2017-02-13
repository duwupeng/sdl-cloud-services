package com.talebase.cloud.ms.exercise.test.controller;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Created by kanghong.zhao on 2017-1-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PaperControllerTest {

    private int port = 28010;
    private String servicePathPre = "";
    private RestTemplate restTemplate = new RestTemplate();

    private ServiceHeader requestHeader;

    MsInvoker msInvoker = new MsInvoker();

    @Before
    public void before(){
        requestHeader = new ServiceHeader();
        requestHeader.customerId = 1;
        requestHeader.customerName = "zhaokh";
        requestHeader.callerFrom = CallerFrom.web;
        requestHeader.callerIP = "127.0.0.1";
        requestHeader.companyId = 1;
        requestHeader.transactionId = "123456789";

        msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
    }

    @Test
    public void test_成功开始考试(){
        String servicePath = "http://localhost:" + port + "/exam/exercise/start";
        TExercise exercise = new TExercise();
        exercise.setUserId(requestHeader.getCustomerId());
        exercise.setPaperId(1);
        exercise.setTaskId(1);

        ServiceResponse<Integer> serviceResponse = msInvoker.post(servicePath, new ServiceRequest(requestHeader, exercise), new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

        assert serviceResponse.getCode() == 0 && serviceResponse.getResponse() > 0;
    }

    @Test
    public void test_成功提交答案(){
        String servicePath = "http://localhost:" + port + "/exam/exercise/submit";
        TExercise exercise = new TExercise();
        exercise.setUserId(requestHeader.getCustomerId());
        exercise.setTaskId(1);
        exercise.setAnswerScoreDetail("{}");
        exercise.setObjScore(new BigDecimal(80));
        exercise.setSubScore(BigDecimal.ZERO);

        ServiceResponse<String> serviceResponse = msInvoker.post(servicePath, new ServiceRequest(requestHeader, exercise), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert serviceResponse.getCode() == 0;
    }

    @Test
    public void test_成功评卷主观题得分(){
        String servicePath = "http://localhost:" + port + "/exam/exercise/subScore/15";
        ServiceResponse<String> serviceResponse = msInvoker.put(servicePath, new ServiceRequest(requestHeader, new BigDecimal(20)), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert serviceResponse.getCode() == 0;
    }

}
