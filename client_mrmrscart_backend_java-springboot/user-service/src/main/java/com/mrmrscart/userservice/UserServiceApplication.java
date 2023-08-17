package com.mrmrscart.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mrmrscart.userservice.util.JsonSchemaValidator;
import com.mrmrscart.userservice.util.PaginatedResponse;
import com.mrmrscart.userservice.util.SSSFileUpload;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
@EnableFeignClients
public class UserServiceApplication {

	@Bean
	public JsonSchemaValidator getJsonSchemaValidatorObject() {
		return new JsonSchemaValidator();
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SSSFileUpload amazonS3Object() {
		return new SSSFileUpload();
	}

	@Bean
	public PaginatedResponse getPaginatedResponseObject() {
		return new PaginatedResponse();
	}
}
