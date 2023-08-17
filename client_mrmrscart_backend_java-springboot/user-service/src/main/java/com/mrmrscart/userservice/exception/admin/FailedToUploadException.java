package com.mrmrscart.userservice.exception.admin;

@SuppressWarnings("serial")
public class FailedToUploadException extends RuntimeException{
	public FailedToUploadException(String message) {
		super(message);
	}
}
