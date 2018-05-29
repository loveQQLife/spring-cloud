package com.kreken.provider.api;

import com.kreken.provider.fallback.FeignHystrixApiFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "user-service",fallback = FeignHystrixApiFallback.class)
public interface FeignHystrixApi {

    @RequestMapping("/test")
    public String test();
}
