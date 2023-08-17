package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class SupplierIdNotFoundException extends RuntimeException{
	public SupplierIdNotFoundException(String message) {
		super(message);
	}
}
