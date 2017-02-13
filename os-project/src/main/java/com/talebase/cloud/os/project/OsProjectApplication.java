package com.talebase.cloud.os.project;

import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.aop.ServiceAspect;
import com.talebase.cloud.os.project.bind.Barista;
import feign.Feign;
import feign.Feign.Builder;
import feign.Logger.Level;
import feign.Request.Options;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

/**
 * Created by bin.yang on 2016-11-24.
 */

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients
@EnableBinding(Barista.class)
public class OsProjectApplication {
    private static final int FIVE_SECONDS = 5000;

    public OsProjectApplication() {
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(OsProjectApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Scope(value = "prototype")
    public Builder feignBuilder() {
        return Feign.builder();
    }

    @Bean
    public Level feignLogger() {
        return Level.FULL;
    }

    @Bean
    public Options options() {
        return new Options(5000, 5000);
    }

    @Bean
    public MsInvoker msInvoker(){return new MsInvoker();}

    @Bean
    public ServiceAspect serviceAspect(){
        return new ServiceAspect();
    }
}