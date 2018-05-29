package com.kreken.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients(basePackages = "com.kreken.api")
@EnableAutoConfiguration
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.kreken")
public class DiscoveryApplication {
	public static void main(String[] args) {
		SpringApplication.run(DiscoveryApplication.class, args);
	}
}
