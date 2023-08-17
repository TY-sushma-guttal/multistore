package com.mrmrscart.authservice.exception.user;

@SuppressWarnings("serial")
public class PasswordNotMatchedException extends RuntimeException {
	public PasswordNotMatchedException(String message) {
		super(message);
	}
}
