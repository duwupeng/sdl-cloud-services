package com.talebase.cloud.ms.examer.test.dto;

import com.talebase.cloud.base.ms.examer.dto.DDataManagementRequest;
import com.talebase.cloud.base.ms.examer.dto.DDataManagementResponse;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by kanghong.zhao on 2017-1-9.
 */
public class DDataManagementResponseTest {

    public static void main(String[] args){
        DDataManagementResponse dto = new DDataManagementResponse();

        dto.setId(1);
        dto.setTotalScore(new BigDecimal(100));
        dto.setUsedTime("60");
        dto.setWhetherMarkDone(true);
        dto.setEndTime(new Timestamp(new Date().getTime()));
//        dto.set

        List<Map<String, Object>> userInfos = new ArrayList<>();

        Map<String, Object> field1 = new HashMap<>();
        field1.put("fieldKey", "name");
        field1.put("fieldName", "姓名");
        field1.put("type", 1);
        field1.put("isexTension", 0);
        field1.put("value", "zhao");
        userInfos.add(field1);

        Map<String, Object> field2 = new HashMap<>();
        field2.put("fieldKey", "mobile");
        field2.put("fieldName", "手机号码");
        field2.put("type", 1);
        field2.put("isexTension", 0);
        field2.put("value", "13580406333");
        userInfos.add(field2);

        Map<String, Object> field3 = new HashMap<>();
        field2.put("fieldKey", "code");
        field2.put("fieldName", "自定义码");
        field2.put("type", 3);
        field2.put("isexTension", 1);
        field2.put("value", "777");
        userInfos.add(field3);

        dto.setUserInfos(userInfos);

        System.out.println(GsonUtil.toJson(dto));

        DDataManagementRequest d = new DDataManagementRequest(1, 1);

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

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageIndex(1);
        pageRequest.setLimit(10);
        pageRequest.setSortFields("score asc");

        ServiceRequest<DDataManagementRequest> req = new ServiceRequest(requestHeader, d, pageRequest);

        System.out.println(GsonUtil.toJson(req));
    }

}
