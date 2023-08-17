package com.mrmrscart.productcategoriesservice.handler.collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.productcategoriesservice.exception.collection.ColorCollectionException;
import com.mrmrscart.productcategoriesservice.response.category.ExceptionResponse;

@RestControllerAdvice
public class ColorCollectionExceptionHandler {

	@ExceptionHandler(value = ColorCollectionException.class)
	public ResponseEntity<ExceptionResponse> collectionExceptionHandler(
			ColorCollectionException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}
}
