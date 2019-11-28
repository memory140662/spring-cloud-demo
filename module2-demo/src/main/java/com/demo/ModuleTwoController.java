package com.demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequestMapping("module-two")
@RestController
public class ModuleTwoController {

    private RestTemplate restTemplate;

    public ModuleTwoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @HystrixCommand(fallbackMethod = "getAllFallbackMethod")
    public List<ModuleTwo> getAll() {
        try {
            ModuleTwo[] forObject = restTemplate.getForObject("http://module-1/module-one", ModuleTwo[].class);
            if (forObject == null) {
                return null;
            }
            return Arrays.asList(forObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ModuleTwo> getAllFallbackMethod() {
        return Collections.emptyList();
    }

    @GetMapping("/{name}")
    @HystrixCommand(fallbackMethod = "getOneFallbackMethod")
    public ModuleTwo getOne(@PathVariable String name) {
        try {
            ResponseEntity<ModuleTwo> resp = restTemplate.getForEntity("http://module-1/module-one/" + name, ModuleTwo.class);
            System.out.println("*************");
            System.out.println(resp.getStatusCode());
            return resp.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getOneFallbackMethod(name);
    }

    public ModuleTwo getOneFallbackMethod(String name) {
        return new ModuleTwo();
    }
}
