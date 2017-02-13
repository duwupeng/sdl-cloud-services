package com.talebase.cloud.os.project.bind;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created by daorong.li on 2016-11-30.
 * mq通讯channel
 */
public interface Barista {
    String  EMAIL_OUTPUT_CHANEL = "email_output_channel";
    String  SMS_OUTPUT_CHANNEL = "sms_output_channel";
    /**
     * 注解@Output声明了它是一个输出类型的通道，名字是email_channel。
     * 表明注入了一个名字为email_channel的通道，类型是output，发布的主题名为email_mq。
     */
    @Output(Barista.EMAIL_OUTPUT_CHANEL)
    MessageChannel sendEmailChanel();

    /**
     * 注解@Output声明了它是一个输出类型的通道，名字是sms_channel。
     * 表明注入了一个名字为sms_channel的通道，类型是output，发布的主题名为sms_mq。
     */
    @Output(Barista.SMS_OUTPUT_CHANNEL)
    MessageChannel sendSmsChanel();

}
