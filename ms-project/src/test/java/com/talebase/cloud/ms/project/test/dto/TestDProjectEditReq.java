package com.talebase.cloud.ms.project.test.dto;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.project.dto.DProjectEditReq;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class TestDProjectEditReq {

    public static void main(String[] args){
        DProjectEditReq dto = new DProjectEditReq();
        dto.setName("新项目");
        dto.setAccountsStr("admin1,admin2,admin3");
        dto.setDescription("描述 ");
        dto.setStartDateStr("2016-01-01 10:00:00");
        dto.setEndDateStr("2016-01-01 12:00:00");

        dto.setScanEnable(true);
        dto.setScanAccountPre("Pre");
        dto.setScanMax(100);
        dto.setScanStartDateStr("2016-01-01 10:00:00");
        dto.setScanEndDateStr("2016-01-01 12:00:00");

        System.out.println(GsonUtil.toJson(new ServiceRequest(new ServiceHeader(), dto)));
    }



}
