package com.mrmrscart.notificationreportlogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition
public class NotificationReportLogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationReportLogServiceApplication.class, args);
	}
}
