package com.kreken.provider.web.back;

import com.alibaba.fastjson.JSONObject;
import com.kreken.provider.api.FeignHystrixApi;
import com.kreken.provider.model.User;
import com.kreken.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BackUserRestful {

    @Autowired
    private UserService userService;
    @Autowired
    private FeignHystrixApi feignHystrixApi;

    @RequestMapping("/back/getUsers")
    public String getAllUser(){
        List<User> list = userService.getUserList();
        return JSONObject.toJSONString(list);
    }

    @RequestMapping("/getTest")
    public String test(){
        return  feignHystrixApi.test();
    }
}
