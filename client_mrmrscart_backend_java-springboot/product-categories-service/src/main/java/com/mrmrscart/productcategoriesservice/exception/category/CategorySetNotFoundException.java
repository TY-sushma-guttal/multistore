package com.mrmrscart.productcategoriesservice.exception.category;

@SuppressWarnings("serial")
public class CategorySetNotFoundException extends RuntimeException{

	public CategorySetNotFoundException(String message) {
		super(message);
	}
}
