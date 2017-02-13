package com.talebase.cloud.os.consumption;

import com.talebase.cloud.common.aop.ServiceAspect;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by suntree.xu on 2016-12-7.
 * 消费中心服务
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class OsConsumptionCenterApplication {
    public  static void main(String arg[]) {
        SpringApplication application = new SpringApplication(OsConsumptionCenterApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(arg);
    }

    @Bean
    public ServiceAspect serviceAspect(){
        return new ServiceAspect();
    }
}
