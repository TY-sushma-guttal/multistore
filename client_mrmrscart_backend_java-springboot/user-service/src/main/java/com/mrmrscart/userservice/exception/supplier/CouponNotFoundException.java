package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class CouponNotFoundException extends RuntimeException {

	public CouponNotFoundException(String message) {
		super(message);
	}
}
