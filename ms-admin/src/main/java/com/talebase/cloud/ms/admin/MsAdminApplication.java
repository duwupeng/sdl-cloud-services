package com.talebase.cloud.ms.admin;

import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.aop.ServiceAspect;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by daorong.li on 2016-11-23.
 * 管理员设置服务
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class MsAdminApplication {
    public  static void main(String args[]){
        SpringApplication application = new SpringApplication(MsAdminApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    public static final String OUTERADMIN = "顾问";
    public static final String EXAMINER = "评卷人";
    public static final String SUPERADMIN = "超级管理员";

    @Bean
    public ServiceAspect serviceAspect(){
        return new ServiceAspect();
    }

}
