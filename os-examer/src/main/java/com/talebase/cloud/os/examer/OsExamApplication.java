package com.talebase.cloud.os.examer;

import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.aop.ServiceAspect;
import feign.Feign;
import feign.Logger;
import feign.Request;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Created by daorong.li on 2016-12-6.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients
@EnableScheduling
public class OsExamApplication {
    public static void main(String arg[]){
        SpringApplication application = new SpringApplication(OsExamApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(arg);
    }


    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }

    @Bean
    public Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }

    private static final int FIVE_SECONDS = 5000;

    @Bean
    public Request.Options options() {
        return new Request.Options(FIVE_SECONDS, FIVE_SECONDS);
    }

    @Bean
    public MsInvoker msInvoker(){
        return new MsInvoker();
    }

    @Bean
    public ServiceAspect serviceAspect(){
        return new ServiceAspect();
    }
}
