package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class CouponNotFoundException extends RuntimeException {

	public CouponNotFoundException(String message) {
		super(message);
	}
}
