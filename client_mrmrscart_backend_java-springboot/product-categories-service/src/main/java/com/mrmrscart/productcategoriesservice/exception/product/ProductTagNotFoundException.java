package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class ProductTagNotFoundException extends RuntimeException {
	public ProductTagNotFoundException(String message) {
		super(message);
	}
}
