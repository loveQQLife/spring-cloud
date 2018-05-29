package com.kreken.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;

@EnableHystrix
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableFeignClients("com.kreken")
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = "com.kreken")
public class UserCenterApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserCenterApplication.class, args);
	}
}
