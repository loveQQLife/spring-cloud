package com.kreken;

import com.kreken.conf.ApplicationContextConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.kreken.feign"})
@EnableCircuitBreaker
@EnableHystrix
@EnableHystrixDashboard
@ComponentScan(basePackages = {"com.kreken"})
public class HystrixApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(HystrixApplication.class,args);
        ApplicationContextConfig.setApplicationContext(applicationContext);
    }
}
