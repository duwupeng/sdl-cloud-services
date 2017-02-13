package com.talebase.cloud.ms.exercise.test.dto;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by kanghong.zhao on 2017-1-14.
 */
public class TestTExercise {

    public static void main(String[] args){
        TExercise exercise = new TExercise();
        exercise.setUserId(1);
        exercise.setTaskId(1);
        exercise.setPaperId(1);

        exercise.setAnswerScoreDetail("{}");
        exercise.setObjScore(new BigDecimal(80));

        //dev环境先用
        ServiceHeader requestHeader = new ServiceHeader();
        requestHeader.customerId = 1;
        requestHeader.customerName = "zhaokh";
        requestHeader.operatorId = 1;
        requestHeader.operatorName = "zhaokh";
        requestHeader.callerFrom = CallerFrom.web;
        requestHeader.callerIP = "127.0.0.1";
        requestHeader.orgCode = "1_";
        requestHeader.companyId = 1;
        requestHeader.transactionId = "123456789";
        requestHeader.setPermissions(Arrays.asList("Permission1", "Permission2", "Permission3", "xxxx"));

        ServiceRequest serviceRequest = new ServiceRequest(requestHeader, exercise);

        System.out.println(GsonUtil.toJson(serviceRequest));

        ServiceResponse<Integer> serviceResponse = new ServiceResponse<>(1);
        System.out.println(GsonUtil.toJson(serviceResponse));

    }


}
