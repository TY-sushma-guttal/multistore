package com.mrmrscart.orderspaymentsservice.exception.order;

@SuppressWarnings("serial")
public class AddressNotFoundException extends RuntimeException {
	public AddressNotFoundException(String message) {
		super(message);
	}
}
