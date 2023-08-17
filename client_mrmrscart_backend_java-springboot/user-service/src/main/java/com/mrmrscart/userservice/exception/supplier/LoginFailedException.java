package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class LoginFailedException extends RuntimeException {
	public LoginFailedException(String message) {
		super(message);
	}

}
