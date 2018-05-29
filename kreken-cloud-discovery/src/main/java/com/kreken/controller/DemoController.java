package com.kreken.controller;

import com.kreken.api.FeignClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @Autowired
    private DiscoveryClient client;
    @Autowired
    private FeignClients feignClient;

    @RequestMapping("/getOne")
    public List  getOne(@RequestParam String name){
        List<ServiceInstance> serviceInstanceList = client.getInstances(name);
        return serviceInstanceList;
    }

    @RequestMapping("/getAllService")
    public List  getAllService(){
        List<String> list = client.getServices();
        return list;
    }

    public List error(){
        return null;
    }

    @RequestMapping("/getUser")
    public String getUsers(){
        return feignClient.getUsers();
    }

}
