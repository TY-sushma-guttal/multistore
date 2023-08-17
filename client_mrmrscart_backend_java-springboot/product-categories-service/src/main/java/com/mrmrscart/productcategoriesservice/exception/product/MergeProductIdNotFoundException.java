package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class MergeProductIdNotFoundException extends RuntimeException{
	public MergeProductIdNotFoundException(String message) {
		super(message);
	}
}
