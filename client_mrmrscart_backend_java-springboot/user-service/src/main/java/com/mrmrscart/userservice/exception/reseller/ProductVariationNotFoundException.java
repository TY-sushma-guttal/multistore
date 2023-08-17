package com.mrmrscart.userservice.exception.reseller;

@SuppressWarnings("serial")
public class ProductVariationNotFoundException extends RuntimeException{
	public ProductVariationNotFoundException(String message) {
		super(message);
	}
}
