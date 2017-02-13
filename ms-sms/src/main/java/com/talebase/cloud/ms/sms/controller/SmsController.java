package com.talebase.cloud.ms.sms.controller;


import com.talebase.cloud.base.ms.sms.TSmsInfo;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talebase.cloud.ms.sms.model.DSmsSendResult;
import com.talebase.cloud.ms.sms.service.SmsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    SmsService smsService;

    @PostMapping("/send")
    public boolean sendSms(@RequestBody TSmsInfo smsInfo) {

        boolean ret = smsService.sendSms(smsInfo);

        return ret;
    }

    @PostMapping("/post")
    public boolean postSms(@RequestBody TSmsInfo smsInfo) {

        boolean ret = smsService.postSms(smsInfo);

        return ret;
    }

    @PostMapping("/queryStatus")
    public ServiceResponse queryStatus(@RequestBody String uuId) {
        int status = smsService.queryStatus(uuId);
        return new ServiceResponse<Integer>(status);
    }

    @PostMapping("/queryStatusList")
    public ServiceResponse queryStatusList(@RequestBody String ids) {
        List<Integer> uuIdList = StringUtil.toIntListByComma(ids);
        List<TSmsInfo> resultList = new ArrayList<TSmsInfo>();
        for (Integer uuId : uuIdList) {
            TSmsInfo tSmsInfo = new TSmsInfo();
            int status = smsService.queryStatus(uuId.toString());
            if (status == -1) {//如果没有记录，则跳过,后期看看如果更改符合逻辑
                continue;
            }
            tSmsInfo.setGuid(uuId.toString());
            tSmsInfo.setStatus(status);
            resultList.add(tSmsInfo);
        }
        return new ServiceResponse(resultList);
    }

    @PostMapping("/callback")
    public boolean handleResult(@RequestBody DSmsSendResult result) {
        return smsService.handleSendResult(result);
    }
}
