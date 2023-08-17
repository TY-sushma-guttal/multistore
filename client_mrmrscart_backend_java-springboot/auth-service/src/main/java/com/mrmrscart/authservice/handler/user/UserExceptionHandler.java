package com.mrmrscart.authservice.handler.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.authservice.exception.user.InvalidCredentailException;
import com.mrmrscart.authservice.exception.user.PasswordNotMatchedException;
import com.mrmrscart.authservice.exception.user.UserNameNotFoundException;
import com.mrmrscart.authservice.response.user.ExceptionResponse;

@RestControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(value = PasswordNotMatchedException.class)
	public ResponseEntity<ExceptionResponse> handleException(PasswordNotMatchedException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = InvalidCredentailException.class)
	public ResponseEntity<ExceptionResponse> handleException(InvalidCredentailException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = UserNameNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleException(UserNameNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
