package com.kreken.feign.fallback;

import com.kreken.feign.FeignUserService;
import org.springframework.stereotype.Component;

@Component
public class FeignUserServiceHystrixFallBack implements FeignUserService {
    @Override
    public String getUserInfo() {
        return "getUserInfo error";
    }

    @Override
    public String getUserInfo1() {
        return "getUserInfo1 error";
    }

    @Override
    public String getUserInfo2() {
        return "getUserInfo2 error";
    }

    @Override
    public String getUserInfo3() {
        return "getUserInfo3 error";
    }
}
