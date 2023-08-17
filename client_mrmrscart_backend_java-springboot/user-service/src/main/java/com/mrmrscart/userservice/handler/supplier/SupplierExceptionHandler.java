package com.mrmrscart.userservice.handler.supplier;

import javax.mail.SendFailedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.userservice.exception.supplier.CouponNotCreatedException;
import com.mrmrscart.userservice.exception.supplier.CouponNotFoundException;
import com.mrmrscart.userservice.exception.supplier.DuplicateCouponCodeException;
import com.mrmrscart.userservice.exception.supplier.InactiveSupplierStoreException;
import com.mrmrscart.userservice.exception.supplier.InvalidBankDetailsException;
import com.mrmrscart.userservice.exception.supplier.InvalidCouponCodeException;
import com.mrmrscart.userservice.exception.supplier.InvalidOtpException;
import com.mrmrscart.userservice.exception.supplier.NoAddressFoundException;
import com.mrmrscart.userservice.exception.supplier.NoBankDetailsFoundException;
import com.mrmrscart.userservice.exception.supplier.PasswordNotMatchedException;
import com.mrmrscart.userservice.exception.supplier.ResetPasswordFailedException;
import com.mrmrscart.userservice.exception.supplier.RoleException;
import com.mrmrscart.userservice.exception.supplier.SellerReviewNotFoundException;
import com.mrmrscart.userservice.exception.supplier.StaffListEmptyException;
import com.mrmrscart.userservice.exception.supplier.StaffManagementException;
import com.mrmrscart.userservice.exception.supplier.StaffManagementInfoNotFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.exception.supplier.SupplierIdNotFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierMobileNumberRegisteredException;
import com.mrmrscart.userservice.exception.supplier.SupplierNotFoundException;
import com.mrmrscart.userservice.exception.supplier.UserIdNotFoundException;
import com.mrmrscart.userservice.response.supplier.ExceptionResponse;
import com.mrmrscart.userservice.response.supplier.SupplierResponse;

@RestControllerAdvice
public class SupplierExceptionHandler {

	@ExceptionHandler(value = NoAddressFoundException.class)
	public ResponseEntity<SupplierResponse> noAddressFoundException(NoAddressFoundException noAddressFoundException) {
		return new ResponseEntity<>(new SupplierResponse(true, noAddressFoundException.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StaffManagementInfoNotFoundException.class)
	public ResponseEntity<ExceptionResponse> supplierExceptionHandler(StaffManagementInfoNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StaffListEmptyException.class)
	public ResponseEntity<ExceptionResponse> supplierExceptionHandler(StaffListEmptyException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StaffManagementException.class)
	public ResponseEntity<ExceptionResponse> supplierExceptionHandler(StaffManagementException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CouponNotCreatedException.class)
	public ResponseEntity<ExceptionResponse> handleException(CouponNotCreatedException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidCouponCodeException.class)
	public ResponseEntity<ExceptionResponse> handleException(InvalidCouponCodeException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SupplierMobileNumberRegisteredException.class)
	public ResponseEntity<ExceptionResponse> handleException(SupplierMobileNumberRegisteredException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = PasswordNotMatchedException.class)
	public ResponseEntity<ExceptionResponse> handleException(PasswordNotMatchedException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = SupplierException.class)
	public ResponseEntity<ExceptionResponse> handleException(SupplierException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = NoBankDetailsFoundException.class)
	public ResponseEntity<ExceptionResponse> supplierExceptionHandler(NoBankDetailsFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidBankDetailsException.class)
	public ResponseEntity<ExceptionResponse> supplierExceptionHandler(InvalidBankDetailsException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidOtpException.class)
	public ResponseEntity<ExceptionResponse> handleException(InvalidOtpException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ResetPasswordFailedException.class)
	public ResponseEntity<ExceptionResponse> handleException(ResetPasswordFailedException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SupplierNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleException(SupplierNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CouponNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleException(CouponNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = RoleException.class)
	public ResponseEntity<ExceptionResponse> handleException(RoleException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SupplierIdNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleException(SupplierIdNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = UserIdNotFoundException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(UserIdNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SellerReviewNotFoundException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(SellerReviewNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InactiveSupplierStoreException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(InactiveSupplierStoreException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateCouponCodeException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(DuplicateCouponCodeException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SendFailedException.class)
	public ResponseEntity<ExceptionResponse> exceptionHandler(SendFailedException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, "Failed To Send Mail", null), HttpStatus.BAD_REQUEST);
	}
}
