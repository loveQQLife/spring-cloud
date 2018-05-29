package com.kreken.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "${service.kreken-provider-uc}",fallback = FeignClientFallBack.class)
public interface FeignClients {

    @RequestMapping("/back/getUsers")
    public String getUsers();
}
