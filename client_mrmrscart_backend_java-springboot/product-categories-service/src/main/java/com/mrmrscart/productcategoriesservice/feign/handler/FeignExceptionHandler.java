package com.mrmrscart.productcategoriesservice.feign.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrmrscart.productcategoriesservice.response.category.ExceptionResponse;

import feign.FeignException;

@RestControllerAdvice
public class FeignExceptionHandler{
	@ExceptionHandler(value = FeignException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(FeignException exception)
			throws JsonProcessingException {
		return new ResponseEntity<>(new ObjectMapper().readValue(exception.contentUTF8(), ExceptionResponse.class),
				HttpStatus.BAD_REQUEST);
	}
}
