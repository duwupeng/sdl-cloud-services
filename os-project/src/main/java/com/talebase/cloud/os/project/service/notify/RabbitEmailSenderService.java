package com.talebase.cloud.os.project.service.notify;

import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.os.project.bind.Barista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by daorong.li on 2016-11-30.
 */
@Service
public class RabbitEmailSenderService {

    @Autowired
    private Barista barista;

    /**
     * 发送邮件
     * @param dEmailList
     * @return
     */
    public ServiceResponse sendEmail(List<DEmailLog> dEmailList){
        try {
            barista.sendEmailChanel().send(MessageBuilder.withPayload(dEmailList).build());
        }catch (Exception e){
            e.printStackTrace();
            return new ServiceResponse(BizEnums.EMAIL_SEND_ERROR);
        }
        return new ServiceResponse();
    }


}
