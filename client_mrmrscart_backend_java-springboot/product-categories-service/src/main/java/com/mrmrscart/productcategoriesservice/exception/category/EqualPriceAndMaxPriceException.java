package com.mrmrscart.productcategoriesservice.exception.category;

@SuppressWarnings("serial")
public class EqualPriceAndMaxPriceException extends RuntimeException{
	public EqualPriceAndMaxPriceException(String message) {
		super(message);
	}
}
