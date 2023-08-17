package com.mrmrscart.productcategoriesservice.exception.product;


@SuppressWarnings("serial")
public class ProductFlagNotFoundException extends RuntimeException{
	public ProductFlagNotFoundException(String message) {
		super(message);
	}
}
