package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class EmptyMasterProductListException extends RuntimeException{
	public EmptyMasterProductListException(String message) {
		super(message);
	}
}
