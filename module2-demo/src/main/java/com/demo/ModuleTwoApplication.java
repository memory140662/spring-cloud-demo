package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix

@RibbonClients(
        value = {
                @RibbonClient(name = "module-1"),
        }
)
public class ModuleTwoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModuleTwoApplication.class, args);
    }

    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return builder.build();
    }
}
