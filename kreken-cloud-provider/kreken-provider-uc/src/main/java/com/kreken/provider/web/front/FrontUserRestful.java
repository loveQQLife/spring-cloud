package com.kreken.provider.web.front;

import com.alibaba.fastjson.JSONObject;
import com.kreken.provider.model.User;
import com.kreken.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FrontUserRestful {

    @Autowired
    private UserService userService;

    @RequestMapping("/front/getUsers")
    public String getAllUser(){
        List<User> list = userService.getUserList();
        return JSONObject.toJSONString(list);
    }
}
