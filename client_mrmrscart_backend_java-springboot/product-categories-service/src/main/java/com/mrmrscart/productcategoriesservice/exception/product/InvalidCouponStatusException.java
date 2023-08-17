package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class InvalidCouponStatusException extends RuntimeException {

	public InvalidCouponStatusException(String message) {
		super(message);
	}
}
