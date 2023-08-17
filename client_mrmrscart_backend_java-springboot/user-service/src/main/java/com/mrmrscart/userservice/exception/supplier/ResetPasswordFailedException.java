package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class ResetPasswordFailedException extends RuntimeException {

	public ResetPasswordFailedException(String message) {
		super(message);
	}
}
