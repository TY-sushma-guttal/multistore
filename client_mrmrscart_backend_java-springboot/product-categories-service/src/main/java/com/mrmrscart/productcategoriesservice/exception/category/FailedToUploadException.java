package com.mrmrscart.productcategoriesservice.exception.category;

@SuppressWarnings("serial")
public class FailedToUploadException extends RuntimeException{
	public FailedToUploadException(String message) {
		super(message);
	}
}
