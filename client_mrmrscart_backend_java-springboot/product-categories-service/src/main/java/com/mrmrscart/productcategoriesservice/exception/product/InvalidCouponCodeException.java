package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class InvalidCouponCodeException extends RuntimeException {

	public InvalidCouponCodeException(String message) {
		super(message);
	}
}
