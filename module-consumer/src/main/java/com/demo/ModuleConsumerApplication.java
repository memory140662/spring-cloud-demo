package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableCircuitBreaker
@RibbonClients(
        value = {
                @RibbonClient(name = "module-1"),
                @RibbonClient(name = "module-2"),
                @RibbonClient(name = "module-3"),
                @RibbonClient(name = "module-4")
        }
)
@EnableCaching
public class ModuleConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleConsumerApplication.class, args);
    }

    @Autowired
    private RestTemplateBuilder templateBuilder;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return templateBuilder.build();
    }

}
