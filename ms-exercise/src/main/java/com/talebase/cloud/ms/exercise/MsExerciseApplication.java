package com.talebase.cloud.ms.exercise;

import com.talebase.cloud.common.aop.ServiceAspect;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by daorong.li on 2016-12-5.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class MsExerciseApplication {
    public static void  main(String arg[]){
        SpringApplication application = new SpringApplication(MsExerciseApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(arg);
    }

    @Bean
    public ServiceAspect serviceAspect(){
        return new ServiceAspect();
    }
}
