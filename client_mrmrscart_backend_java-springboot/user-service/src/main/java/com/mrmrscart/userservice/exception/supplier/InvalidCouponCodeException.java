package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class InvalidCouponCodeException extends RuntimeException {
	public InvalidCouponCodeException(String message) {
		super(message);
	}

}
