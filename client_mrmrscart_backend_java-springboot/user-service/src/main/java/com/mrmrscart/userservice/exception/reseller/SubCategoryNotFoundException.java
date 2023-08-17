package com.mrmrscart.userservice.exception.reseller;

@SuppressWarnings("serial")
public class SubCategoryNotFoundException extends RuntimeException{
	public SubCategoryNotFoundException(String message) {
		super(message);
	}
}
