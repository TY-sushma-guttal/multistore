package com.mrmrscart.helpandsupportservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.helpandsupportservice.exception.EmptyTicketListException;
import com.mrmrscart.helpandsupportservice.exception.HelpSupportException;
import com.mrmrscart.helpandsupportservice.exception.TicketNotFoundException;
import com.mrmrscart.helpandsupportservice.response.SuccessResponse;

@RestControllerAdvice
public class HelpSupportHandler {

	@ExceptionHandler(value = HelpSupportException.class)
	public ResponseEntity<SuccessResponse> helpSupportExceptionHandler(HelpSupportException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = TicketNotFoundException.class)
	public ResponseEntity<SuccessResponse> helpSupportExceptionHandler(TicketNotFoundException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EmptyTicketListException.class)
	public ResponseEntity<SuccessResponse> resellerExceptionHandler(EmptyTicketListException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
