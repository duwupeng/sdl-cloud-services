package com.talebase.cloud.ms.project.test.dto;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.project.dto.DProjectQueryReq;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;

import java.util.Arrays;

/**
 * Created by kanghong.zhao on 2016-12-5.
 */
public class TestDProjectQueryReq {

    public static void main(String[] args){
        DProjectQueryReq queryReq = new DProjectQueryReq();
        queryReq.setTaskNameLike("任务");
        queryReq.setProjectNameLike("项目");

        PageRequest pageRequest = new PageRequest();
        pageRequest.setLimit(15);
        pageRequest.setSortFields("by id desc");

        //dev环境先用
        ServiceHeader requestHeader = new ServiceHeader();
        requestHeader = new ServiceHeader();
        requestHeader.customerId = 1;
        requestHeader.customerName = "zhaokh";
        requestHeader.operatorId = 1;
        requestHeader.operatorName = "zhaokh";
        requestHeader.callerFrom = CallerFrom.web;
        requestHeader.callerIP = "127.0.0.1";
        requestHeader.orgCode = "1_";
        requestHeader.companyId = 1;
        requestHeader.transactionId = "123456789";
        requestHeader.setPermissions(Arrays.asList("Permission1", "Permission2", "Permission3"));

        ServiceRequest sr = new ServiceRequest(requestHeader, queryReq, pageRequest);

        System.out.println(GsonUtil.toJson(sr));
    }


}
