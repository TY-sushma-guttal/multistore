package com.mrmrscart.notificationreportlogservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.notificationreportlogservice.exception.MarketingToolPriceChangeException;
import com.mrmrscart.notificationreportlogservice.exception.NoCustomerFoundException;
import com.mrmrscart.notificationreportlogservice.exception.NoRecordFoundException;
import com.mrmrscart.notificationreportlogservice.exception.NotificationNotFoundException;
import com.mrmrscart.notificationreportlogservice.exception.PushNotificationSuggestionException;
import com.mrmrscart.notificationreportlogservice.exception.UserIdNotFoundException;
import com.mrmrscart.notificationreportlogservice.response.SupplierResponse;

@RestControllerAdvice
public class NotificationReportLogExceptionHandler {

	@ExceptionHandler(value = NoRecordFoundException.class)
	public ResponseEntity<SupplierResponse> exceptionHandler(NoRecordFoundException exception) {
		return new ResponseEntity<>(new SupplierResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MarketingToolPriceChangeException.class)
	public ResponseEntity<SupplierResponse> exceptionHandler(MarketingToolPriceChangeException exception) {
		return new ResponseEntity<>(new SupplierResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = NotificationNotFoundException.class)
	public ResponseEntity<SupplierResponse> exceptionHandler(NotificationNotFoundException exception) {
		return new ResponseEntity<>(new SupplierResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = NoCustomerFoundException.class)
	public ResponseEntity<SupplierResponse> exceptionHandler(NoCustomerFoundException exception) {
		return new ResponseEntity<>(new SupplierResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = PushNotificationSuggestionException.class)
	public ResponseEntity<SupplierResponse> exceptionHandler(PushNotificationSuggestionException exception) {
		return new ResponseEntity<>(new SupplierResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(value = UserIdNotFoundException.class)
	public ResponseEntity<SupplierResponse> exceptionHandler(UserIdNotFoundException exception) {
		return new ResponseEntity<>(new SupplierResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
