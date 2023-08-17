package com.mrmrscart.productcategoriesservice.exception.category;

@SuppressWarnings("serial")
public class VariationNameExistsException extends RuntimeException {

	public VariationNameExistsException(String message) {
		super(message);
	}
}
