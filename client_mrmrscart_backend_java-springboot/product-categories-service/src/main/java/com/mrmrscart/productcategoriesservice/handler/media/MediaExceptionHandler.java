package com.mrmrscart.productcategoriesservice.handler.media;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.productcategoriesservice.exception.media.EmptyMediaException;
import com.mrmrscart.productcategoriesservice.exception.media.InvalidFilePathException;
import com.mrmrscart.productcategoriesservice.exception.media.MediaException;
import com.mrmrscart.productcategoriesservice.exception.media.UnacceptedImageTypeException;
import com.mrmrscart.productcategoriesservice.response.category.ExceptionResponse;

@RestControllerAdvice
public class MediaExceptionHandler {
	@ExceptionHandler(value = UnacceptedImageTypeException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(
			UnacceptedImageTypeException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MediaException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(
			MediaException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = EmptyMediaException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(
			EmptyMediaException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = InvalidFilePathException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(
			InvalidFilePathException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}
}
