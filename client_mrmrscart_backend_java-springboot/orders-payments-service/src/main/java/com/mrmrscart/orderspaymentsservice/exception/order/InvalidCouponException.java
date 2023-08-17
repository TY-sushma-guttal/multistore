package com.mrmrscart.orderspaymentsservice.exception.order;

@SuppressWarnings("serial")
public class InvalidCouponException extends RuntimeException {
	public InvalidCouponException(String message) {
		super(message);
	}
}
