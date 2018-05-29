package com.kreken.controller;

import com.kreken.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ClientController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String add(@RequestParam(value = "id") Long id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        String str = userService.getUserInfo(map);
        return str;
    }

}