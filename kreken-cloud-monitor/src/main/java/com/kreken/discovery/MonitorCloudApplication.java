package com.kreken.discovery;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableTurbine
@EnableHystrixDashboard
@EnableAdminServer
@ComponentScan(basePackages = {"com.kreken"})
public class MonitorCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorCloudApplication.class, args);
	}
}
