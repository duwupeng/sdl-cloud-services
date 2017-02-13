package com.talebase.cloud.ms.email.bind;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

/**
 * Created by daorong.li on 2016-11-30.
 * mq通讯channel
 */
public interface Barista {
    String  EMAIL_INPUT_CHANEL = "email_input_channel";

    /**
     * 注解@Output声明了它是一个输出类型的通道，名字是email_channel。
     * 表明注入了一个名字为email_channel的通道，类型是output，发布的主题名为email_mq。
     */
    @Input(Barista.EMAIL_INPUT_CHANEL)
    MessageChannel emailReceiver();
}
