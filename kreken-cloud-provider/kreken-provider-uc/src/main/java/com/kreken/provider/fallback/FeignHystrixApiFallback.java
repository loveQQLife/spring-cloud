package com.kreken.provider.fallback;

import com.kreken.provider.api.FeignHystrixApi;
import org.springframework.stereotype.Component;

@Component
public class FeignHystrixApiFallback implements FeignHystrixApi {

    @Override
    public String test(){
        return "error";
    }
}
