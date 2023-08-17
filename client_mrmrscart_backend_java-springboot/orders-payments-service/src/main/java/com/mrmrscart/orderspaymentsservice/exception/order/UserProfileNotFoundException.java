package com.mrmrscart.orderspaymentsservice.exception.order;

@SuppressWarnings("serial")
public class UserProfileNotFoundException extends RuntimeException {
	public UserProfileNotFoundException(String message) {
		super(message);
	}
}
