package com.talebase.cloud.ms.sms.mq.receiver;


import com.talebase.cloud.base.ms.sms.TSmsInfo;
import com.talebase.cloud.ms.sms.mq.bind.Barista;
import com.talebase.cloud.ms.sms.service.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import java.util.Map;

/**
 * Created by eric on 16/11/16.
 */
@EnableBinding(Barista.class)
public class RabbitReceiver {

    @Autowired
    SmsSender smsSender;

    @StreamListener(Barista.INPUT_CHANNEL)
    public void receiver(Message<TSmsInfo> message){
        TSmsInfo obj = message.getPayload();
        System.out.println("接受对象Channel:"+obj+"\n");
        smsSender.senSms(obj);
    }

    @StreamListener(Barista.INPUT_CHANNEL_ANOTHER)
    public void receiverAnotherChannel(Message<Object> message){
        Object obj = message.getPayload();
        System.out.println("接受对象AnotherChannel:"+obj+"\n");
    }

}
