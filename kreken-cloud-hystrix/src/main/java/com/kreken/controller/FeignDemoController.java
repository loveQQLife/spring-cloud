package com.kreken.controller;

import com.kreken.feign.FeignUserService;
import com.kreken.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FeignDemoController {

    @Autowired
    private FeignUserService feignUserService;
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/feign")
    public String getUserInfo(HttpServletRequest request, String id){
        User user = new User();
        user.setUuid(String.valueOf(id));
        user.setPhone("13726040244");
        user.setUsername("interface");
        String str = feignUserService.getUserInfo();
        return str;
    }


    @RequestMapping("/feign1")
    public String getUserInfo1(HttpServletRequest request, String id){
        User user = new User();
        user.setUuid(String.valueOf(id));
        user.setPhone("13726040244");
        user.setUsername("interface");
        String str = feignUserService.getUserInfo1();
        return str;
    }

    @RequestMapping("/feign2")
    public String getUserInfo2(HttpServletRequest request, String id){
        User user = new User();
        user.setUuid(String.valueOf(id));
        user.setPhone("13726040244");
        user.setUsername("interface");
        String str = feignUserService.getUserInfo2();
        return str;
    }
    @RequestMapping("/feign3")
    public String getUserInfo3(HttpServletRequest request, String id){
        User user = new User();
        user.setUuid(String.valueOf(id));
        user.setPhone("13726040244");
        user.setUsername("interface");
        String str = feignUserService.getUserInfo3();
        return str;
    }

    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
