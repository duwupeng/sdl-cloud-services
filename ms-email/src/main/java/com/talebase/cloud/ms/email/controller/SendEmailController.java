package com.talebase.cloud.ms.email.controller;



//import SendEmailService;

import com.talebase.cloud.base.ms.common.domain.TEmailLog;
import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.email.bind.Barista;
import com.talebase.cloud.ms.email.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by daorong.li on 2016-11-15.
 */
@RestController
public class SendEmailController {
    @Autowired
    private SendEmailService sendEmailService;

    /**
     * 发送邮件
     * @param message
     */
    @StreamListener(Barista.EMAIL_INPUT_CHANEL)
    public void receiver(Message<List<DEmailLog>> message){
        List<DEmailLog> emailList = message.getPayload();
        sendEmailService.sendEmail(emailList);
    }

    @PostMapping(value = "/emails")
    public ServiceResponse<List<TEmailLog>> getEmails(@RequestBody ServiceRequest<DEmailLog> req){
        DEmailLog log  = req.getRequest();
        List<TEmailLog> list = sendEmailService.getEmails(log);
        ServiceResponse<List<TEmailLog>> response = new  ServiceResponse<List<TEmailLog>>(list);
        return response;
    }

}
