package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class SupplierListEmptyException extends RuntimeException{
	public SupplierListEmptyException(String message) {
		super(message);
	}
}
