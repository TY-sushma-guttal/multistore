package com.mrmrscart.userservice.handler.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.userservice.exception.customer.CustomerException;
import com.mrmrscart.userservice.response.supplier.ExceptionResponse;

@RestControllerAdvice
public class CustomerExceptionHandler {

	@ExceptionHandler(value = CustomerException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(CustomerException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
