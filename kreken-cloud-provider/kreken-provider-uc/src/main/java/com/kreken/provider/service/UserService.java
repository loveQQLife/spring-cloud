package com.kreken.provider.service;

import com.kreken.provider.model.User;
import com.kreken.provider.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService<User,UserMapper> {
    @Override
    protected String getTableName() {
        return "用户表";
    }

    public List<User> getUserList(){
        return mapper.getAllUser();
    }
}
