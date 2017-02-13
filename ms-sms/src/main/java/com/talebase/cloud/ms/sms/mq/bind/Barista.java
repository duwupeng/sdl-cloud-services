package com.talebase.cloud.ms.sms.mq.bind;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by eric on 16/11/16.
 */
public interface Barista {
    String INPUT_CHANNEL = "input_channel";
    String INPUT_CHANNEL_ANOTHER = "input_channel_another";


//    String OUTPUT_CHANNEL = "output_channel";

//    String OUTPUT1 = "output1";

    /***
     *     注解@Input声明了它是一个输入类型的通道，名字是Barista.INPUT_CHANNEL，
     *     也就是position3的input_channel。这一名字与上述配置app2的配置文件中position1应该一致，
     *     表明注入了一个名字叫做input_channel的通道，它的类型是input，订阅的主题是position2处声明的mydest这个主题
     */

    @Input(Barista.INPUT_CHANNEL)
    SubscribableChannel logInput();


    /**
     * 注解@Output声明了它是一个输出类型的通道，名字是output_channel。
     * 这一名字与app1中通道名一致，表明注入了一个名字为output_channel的通道，类型是output，发布的主题名为mydest。</span>
     */
//    @Output(Barista.OUTPUT_CHANNEL)
//    MessageChannel logOutPut();

        @Input(Barista.INPUT_CHANNEL_ANOTHER)
        SubscribableChannel input();
//
//    @Output(Barista.OUTPUT1)
//    MessageChannel output1();
}
