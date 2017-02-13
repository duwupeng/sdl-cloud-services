package com.talebase.cloud.os.project.controller.notify;

import com.talebase.cloud.base.ms.sms.TSmsInfo;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.os.project.service.notify.RabbitSmsSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by suntree.xu on 2016-12-5.
 */
@RestController
public class SendSmsController {

    @Autowired
    RabbitSmsSenderService smsSenderService;

    @PostMapping(value = "sms/sendSms/{phoneNo}")
    public ServiceResponse sendSms(@PathVariable("phoneNo") String request){
        TSmsInfo smsInfo = new TSmsInfo();
        smsInfo.setGuid(UUID.randomUUID().toString());
        smsInfo.setSendto(request);
        smsInfo.setContent("543210");
        smsSenderService.smsSender(smsInfo);
        System.out.println("发送了"+smsInfo);
        return new ServiceResponse();
    }
}
