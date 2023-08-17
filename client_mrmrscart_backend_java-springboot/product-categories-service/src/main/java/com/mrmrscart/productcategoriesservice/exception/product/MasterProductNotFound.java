package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class MasterProductNotFound extends RuntimeException{
	public MasterProductNotFound(String message) {
		super(message);
	}
}
