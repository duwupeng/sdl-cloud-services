package com.talebase.cloud.ms.project;

import com.talebase.cloud.common.aop.ServiceAspect;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MsProjectApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MsProjectApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
    @Bean
    public ServiceAspect serviceAspect(){
        return new ServiceAspect();
    }
}
