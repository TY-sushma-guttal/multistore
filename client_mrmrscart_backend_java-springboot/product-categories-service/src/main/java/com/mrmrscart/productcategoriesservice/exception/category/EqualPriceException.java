package com.mrmrscart.productcategoriesservice.exception.category;

@SuppressWarnings("serial")
public class EqualPriceException extends RuntimeException{
	public EqualPriceException(String message) {
		super(message);
	}
}
