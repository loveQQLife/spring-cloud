package com.kreken.feign;

import com.kreken.feign.fallback.FeignUserServiceHystrixFallBack;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
@Component
@FeignClient(value = "kreken-provider-uc",fallback = FeignUserServiceHystrixFallBack.class)
public interface FeignUserService {

    @RequestMapping("/back/getUsers")
    String getUserInfo();

    @RequestMapping("/back/getUsers")
    String getUserInfo1();

    @RequestMapping("/back/getUsers")
    String getUserInfo2();

    @RequestMapping("/back/getUsers")
    String getUserInfo3();
}
