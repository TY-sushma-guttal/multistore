package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class PasswordNotMatchedException extends RuntimeException {
	public PasswordNotMatchedException(String message) {
		super(message);
	}
}
