package com.mrmrscart.userservice.handler.reseller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.userservice.exception.reseller.InvalidSubscriptionException;
import com.mrmrscart.userservice.exception.reseller.MainCategoryNotFoundException;
import com.mrmrscart.userservice.exception.reseller.MarketingToolException;
import com.mrmrscart.userservice.exception.reseller.MarketingToolNotFound;
import com.mrmrscart.userservice.exception.reseller.MarketingToolThemeException;
import com.mrmrscart.userservice.exception.reseller.MarketingToolThemeNotFound;
import com.mrmrscart.userservice.exception.reseller.MasterProductNotFoundException;
import com.mrmrscart.userservice.exception.reseller.ProductVariationNotFoundException;
import com.mrmrscart.userservice.exception.reseller.PuschasedMarketingToolHistoryNotFoundException;
import com.mrmrscart.userservice.exception.reseller.SubCategoryNotFoundException;
import com.mrmrscart.userservice.response.supplier.ExceptionResponse;

@RestControllerAdvice
 public class ResellerExceptionHandler {
	
	@ExceptionHandler(value = MasterProductNotFoundException.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(MasterProductNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = ProductVariationNotFoundException.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(ProductVariationNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = SubCategoryNotFoundException.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(SubCategoryNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MainCategoryNotFoundException.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(MainCategoryNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MarketingToolException.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(MarketingToolException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MarketingToolThemeException.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(MarketingToolThemeException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MarketingToolThemeNotFound.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(MarketingToolThemeNotFound exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = InvalidSubscriptionException.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(InvalidSubscriptionException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MarketingToolNotFound.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(MarketingToolNotFound exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = PuschasedMarketingToolHistoryNotFoundException.class)
	public ResponseEntity<ExceptionResponse> resellerExceptionHandler(PuschasedMarketingToolHistoryNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
