package com.mrmrscart.userservice.handler.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.userservice.exception.admin.AdminException;
import com.mrmrscart.userservice.exception.admin.AdminManagerNotFound;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolFilterException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolNotFoundException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolsCampaignException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolsCampaignNotFoundException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolsException;
import com.mrmrscart.userservice.exception.admin.AdminNotFoundException;
import com.mrmrscart.userservice.exception.admin.AdminRegistrationException;
import com.mrmrscart.userservice.exception.admin.BannerException;
import com.mrmrscart.userservice.exception.admin.BannerIdNotPresentException;
import com.mrmrscart.userservice.exception.admin.BannerListIsEmptyException;
import com.mrmrscart.userservice.exception.admin.FailedToUploadException;
import com.mrmrscart.userservice.exception.admin.FilterConditionException;
import com.mrmrscart.userservice.exception.admin.MarketingToolStatusException;
import com.mrmrscart.userservice.exception.admin.MarketingToolSubscriptionNotFoundException;
import com.mrmrscart.userservice.exception.admin.ToolSubscriptionExpiredException;
import com.mrmrscart.userservice.exception.reseller.ActiveMarketingToolSubscriptionSaveFailedException;
import com.mrmrscart.userservice.exception.reseller.PurchaseHistorySaveFailedException;
import com.mrmrscart.userservice.response.supplier.ExceptionResponse;

@RestControllerAdvice
public class AdminExceptionHandler {
	@ExceptionHandler(value = AdminRegistrationException.class)
	public ResponseEntity<ExceptionResponse> supplierExceptionHandler(AdminRegistrationException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AdminException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(AdminException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = BannerException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(BannerException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = BannerIdNotPresentException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(BannerIdNotPresentException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = BannerListIsEmptyException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(BannerListIsEmptyException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AdminMarketingToolsException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(AdminMarketingToolsException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MarketingToolStatusException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(MarketingToolStatusException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = AdminMarketingToolsCampaignException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(AdminMarketingToolsCampaignException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AdminMarketingToolsCampaignNotFoundException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(AdminMarketingToolsCampaignNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AdminMarketingToolFilterException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(AdminMarketingToolFilterException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = FilterConditionException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(FilterConditionException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MarketingToolSubscriptionNotFoundException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(MarketingToolSubscriptionNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ToolSubscriptionExpiredException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(ToolSubscriptionExpiredException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AdminMarketingToolNotFoundException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(AdminMarketingToolNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = PurchaseHistorySaveFailedException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(PurchaseHistorySaveFailedException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ActiveMarketingToolSubscriptionSaveFailedException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(
			ActiveMarketingToolSubscriptionSaveFailedException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = FailedToUploadException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(FailedToUploadException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AdminNotFoundException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(AdminNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AdminManagerNotFound.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(AdminManagerNotFound exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
