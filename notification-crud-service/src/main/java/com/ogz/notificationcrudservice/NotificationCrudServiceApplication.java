package com.ogz.notificationcrudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.ogz.client")
@EnableDiscoveryClient
public class NotificationCrudServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationCrudServiceApplication.class, args);
	}

}
