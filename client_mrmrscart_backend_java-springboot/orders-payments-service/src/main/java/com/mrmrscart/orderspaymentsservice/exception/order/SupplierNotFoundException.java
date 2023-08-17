package com.mrmrscart.orderspaymentsservice.exception.order;

@SuppressWarnings("serial")
public class SupplierNotFoundException extends RuntimeException {
	public SupplierNotFoundException(String message) {
		super(message);
	}
}
