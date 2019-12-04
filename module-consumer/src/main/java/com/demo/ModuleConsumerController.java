package com.demo;

import com.demo.entities.Module;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@SuppressWarnings({"unchecked"})
public class ModuleConsumerController {

    @Value("#{environment.CLEAR_CACHE ?: false}")
    private Boolean clearCache;

    private static final String REST_URL_MODULE_ONE_PREFIX = "http://module-1/module-one";
    private static final String REST_URL_MODULE_TWO_PREFIX = "http://module-2/module-two";
    private static final String REST_URL_MODULE_THREE_PREFIX = "http://module-3/module-three";
    private static final String REST_URL_MODULE_FOUR_PREFIX = "http://module-4/module-four";

    private RestTemplate restTemplate;

    private CacheManager cacheManager;

    public ModuleConsumerController(RestTemplate restTemplate, CacheManager cacheManager) {
        this.restTemplate = restTemplate;
        this.cacheManager = cacheManager;
    }

    @GetMapping
    @HystrixCommand(fallbackMethod = "getAllFallback")
    public List<List<Module>> getAll(HttpServletRequest request, HttpServletResponse response) {
        if (response != null) {
            response.addHeader("Cache-Control", "no-cache");
        }
        List<Module> modules1 = restTemplate.getForObject(REST_URL_MODULE_ONE_PREFIX + "?l=" + Math.random(), List.class);
        List<Module> modules2 = restTemplate.getForObject(REST_URL_MODULE_TWO_PREFIX + "?l=" + Math.random(), List.class);
        List<Module> modules3 = restTemplate.getForObject(REST_URL_MODULE_THREE_PREFIX + "?l=" + Math.random(), List.class);
        List<Module> modules4 = restTemplate.getForObject(REST_URL_MODULE_FOUR_PREFIX  + "?l=" + Math.random(), List.class);
        if (clearCache) {
            clearCache();
        }
        return Arrays.asList(modules1, modules2, modules3, modules4);
    }

    private void clearCache() {
        System.out.println("clearCache");
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
    }

    public List<List<Module>>  getAllFallback(HttpServletRequest request, HttpServletResponse response) {
        return Collections.emptyList();
    }


    @GetMapping("/async")
    @HystrixCommand(fallbackMethod = "getAsyncAllFallback")
    public List<List<Module>> getAsyncAll(HttpServletRequest request, HttpServletResponse response) throws ExecutionException, InterruptedException {
        if (response != null) {
            response.addHeader("Cache-Control", "no-cache");
        }
        CompletableFuture<List<Module>> asyncModules1 = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(REST_URL_MODULE_ONE_PREFIX + "?l=" + Math.random(), List.class));
        CompletableFuture<List<Module>> asyncModules2 = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(REST_URL_MODULE_TWO_PREFIX + "?l=" + Math.random(), List.class));
        CompletableFuture<List<Module>> asyncModules3 = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(REST_URL_MODULE_THREE_PREFIX + "?l=" + Math.random(), List.class));
        CompletableFuture<List<Module>> asyncModules4 = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(REST_URL_MODULE_FOUR_PREFIX + "?l=" + Math.random(), List.class));

        CompletableFuture
                .allOf(asyncModules1, asyncModules2, asyncModules3, asyncModules4)
                .get();

        if (clearCache) {
            clearCache();
        }
        return Arrays.asList(asyncModules1.get(), asyncModules2.get(), asyncModules3.get(), asyncModules4.get());
    }

    public List<List<Module>>  getAsyncAllFallback(HttpServletRequest request, HttpServletResponse response) {
        return Collections.emptyList();
    }
}
