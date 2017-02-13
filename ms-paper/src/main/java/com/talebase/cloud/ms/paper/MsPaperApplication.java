package com.talebase.cloud.ms.paper;
import com.talebase.cloud.common.aop.ServiceAspect;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * Created by bin.yang on 2016-11-24.
 */

@SpringBootApplication
@EnableDiscoveryClient

public class MsPaperApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MsPaperApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Bean
    public ServiceAspect serviceAspect(){
        return new ServiceAspect();
    }
}