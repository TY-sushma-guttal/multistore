package com.mrmrscart.orderspaymentsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrdersPaymentsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersPaymentsServiceApplication.class, args);
	}

}
