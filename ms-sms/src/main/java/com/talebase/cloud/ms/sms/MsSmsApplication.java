package com.talebase.cloud.ms.sms;


import com.talebase.cloud.common.aop.ServiceAspect;
import com.talebase.cloud.ms.sms.mq.bind.Barista;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableBinding(Barista.class)
public class MsSmsApplication {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(MsSmsApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
	}

	@Bean
	public ServiceAspect serviceAspect(){
		return new ServiceAspect();
	}
}
