package com.kreken.api;

import org.springframework.stereotype.Component;

@Component
public class FeignClientFallBack  implements FeignClients{

    public String getUsers(){
        return "getUsers error";
    }
}
