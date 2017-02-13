package com.talebase.cloud.os.project.service.notify;


import com.talebase.cloud.os.project.bind.Barista;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by suntree.xu on 2016-12-5.
 */
@Service
public class RabbitSmsSenderService {
    @Autowired
    private Barista source;

    Logger logger = Logger.getLogger(RabbitSmsSenderService.class);

    public void smsSender(Object msg){
        try{
            source.sendSmsChanel().send(MessageBuilder.withPayload(msg).build());
            //System.out.println("发送了"+msg);
            logger.info("发送了"+msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

