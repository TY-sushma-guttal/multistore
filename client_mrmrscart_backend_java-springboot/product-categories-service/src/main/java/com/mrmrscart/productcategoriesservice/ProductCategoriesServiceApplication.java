package com.mrmrscart.productcategoriesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.mrmrscart.productcategoriesservice.util.SSSFileUpload;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;


@SpringBootApplication
@EnableMongoAuditing
@OpenAPIDefinition
@EnableFeignClients
public class ProductCategoriesServiceApplication {

	@Bean
	public SSSFileUpload amazonS3Object() {
		return new SSSFileUpload();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ProductCategoriesServiceApplication.class, args);
	}
}
