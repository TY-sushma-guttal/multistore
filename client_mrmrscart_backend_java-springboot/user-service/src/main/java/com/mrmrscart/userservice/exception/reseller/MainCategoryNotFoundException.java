package com.mrmrscart.userservice.exception.reseller;

@SuppressWarnings("serial")
public class MainCategoryNotFoundException extends RuntimeException{
	public MainCategoryNotFoundException(String message) {
		super(message);
	}
}
