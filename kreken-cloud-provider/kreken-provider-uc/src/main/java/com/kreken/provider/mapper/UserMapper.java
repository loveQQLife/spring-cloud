package com.kreken.provider.mapper;

import com.kreken.provider.model.User;
import com.kreken.provider.tkmapper.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

    @Select("select * from user")
    List<User> getAllUser();
}
