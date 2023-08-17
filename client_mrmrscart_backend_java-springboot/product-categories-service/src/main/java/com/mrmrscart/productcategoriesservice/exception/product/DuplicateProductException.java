package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class DuplicateProductException extends RuntimeException {

	public DuplicateProductException(String message) {
		super(message);
	}
}
