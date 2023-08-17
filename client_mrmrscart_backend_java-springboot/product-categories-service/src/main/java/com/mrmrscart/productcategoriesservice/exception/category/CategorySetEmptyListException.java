package com.mrmrscart.productcategoriesservice.exception.category;

@SuppressWarnings("serial")
public class CategorySetEmptyListException extends RuntimeException{
	public CategorySetEmptyListException(String message) {
		super(message);
	}
}
