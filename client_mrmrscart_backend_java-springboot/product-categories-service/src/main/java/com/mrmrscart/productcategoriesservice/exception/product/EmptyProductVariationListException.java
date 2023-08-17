package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class EmptyProductVariationListException extends RuntimeException{
	
	
	public EmptyProductVariationListException(String message) {
		super(message);
	}
}
