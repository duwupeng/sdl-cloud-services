package com.talebase.cloud.ms.email;

import com.talebase.cloud.common.aop.ServiceAspect;
import com.talebase.cloud.ms.email.bind.Barista;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by daorong.li on 2016-11-21.
 * 邮件发送服务
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableBinding(Barista.class)
public class MsEmailApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MsEmailApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Bean
    public ServiceAspect serviceAspect(){
        return new ServiceAspect();
    }
}
