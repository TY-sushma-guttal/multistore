package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class InvalidSkuException extends RuntimeException{
	public InvalidSkuException(String message) {
		super(message);
	}
}
