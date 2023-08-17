package com.mrmrscart.productcategoriesservice.exception.category;

@SuppressWarnings("serial")
public class MinPriceAndEqualPriceException extends RuntimeException{
	public MinPriceAndEqualPriceException(String message) {
		super(message);
	}
}
