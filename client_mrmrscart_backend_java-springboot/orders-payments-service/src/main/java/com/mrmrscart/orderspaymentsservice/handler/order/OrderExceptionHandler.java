package com.mrmrscart.orderspaymentsservice.handler.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.orderspaymentsservice.exception.order.OrderException;
import com.mrmrscart.orderspaymentsservice.response.order.ExceptionResponse;

@RestControllerAdvice
public class OrderExceptionHandler {

	@ExceptionHandler(value = OrderException.class)
	public ResponseEntity<ExceptionResponse> supplierExceptionHandler(OrderException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

}
