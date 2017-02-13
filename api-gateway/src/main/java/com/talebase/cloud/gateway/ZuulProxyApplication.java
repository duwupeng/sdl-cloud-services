package com.talebase.cloud.gateway;

import com.talebase.cloud.gateway.filter.AccessFilter;
import com.talebase.cloud.gateway.listener.UrlAuthContextListener;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.BoundHashOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 16/11/7.
 */
@EnableZuulProxy
@SpringCloudApplication
@EnableEurekaClient
@EnableFeignClients
@ServletComponentScan
public class ZuulProxyApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ZuulProxyApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }
}
