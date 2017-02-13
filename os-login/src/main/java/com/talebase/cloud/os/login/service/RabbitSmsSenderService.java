package com.talebase.cloud.os.login.service;

import com.talebase.cloud.base.ms.admin.domain.TRole;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.sms.TSmsInfo;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.SmsUtil;
import com.talebase.cloud.os.login.bind.Barista;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by suntree.xu on 2016-12-5.
 */
@Service
public class RabbitSmsSenderService {
    @Autowired
    private Barista source;

    @Autowired
    MsInvoker msInvoker;

    final static String CONSUMPTION_SERVICE_NAME ="ms-consumption";

    Logger logger = Logger.getLogger(RabbitSmsSenderService.class);

    public void smsSender(TSmsInfo msg){
        String servicePath = "http://" + CONSUMPTION_SERVICE_NAME + "/consume/operate";
        TAccountLine tAccountLine = new TAccountLine();
        tAccountLine.setSmsVar(SmsUtil.calculateSmsCount(msg.getContent()));
        tAccountLine.setModifier(msg.getSendto());
        tAccountLine.setType(1);
        ServiceResponse<String> rolesResponse = msInvoker.put(servicePath,new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(),tAccountLine), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        if(rolesResponse.getCode() == 0){
            source.sendSmsChanel().send(MessageBuilder.withPayload(msg).build());
            logger.info("发送了“" + msg.getContent() + "”到" + msg.getSendto());
        }
    }
}

