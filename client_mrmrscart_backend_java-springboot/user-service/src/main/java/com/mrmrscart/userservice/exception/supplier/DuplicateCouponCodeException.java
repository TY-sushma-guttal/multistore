package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class DuplicateCouponCodeException extends RuntimeException {
	public DuplicateCouponCodeException(String message) {
		super(message);

	}
}
