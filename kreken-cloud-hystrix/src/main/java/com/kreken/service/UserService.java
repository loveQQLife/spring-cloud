package com.kreken.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @HystrixCommand(fallbackMethod = "hiError")
    public String getUserInfo(Map<String, String> map) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("provider");
        logger.info("调用服务：" + serviceInstance.getServiceId() + " ,hostname:" + serviceInstance.getHost() + ":" + serviceInstance.getPort());
        String str = restTemplate.getForEntity("http://kreken-provider-uc/back/getUsers", String.class).getBody();
        return str;
    }

    public String hiError(Map<String, String> map) {
        return "error";
    }

}
