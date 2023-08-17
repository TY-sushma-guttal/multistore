package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class InvalidOtpException extends RuntimeException {
	public InvalidOtpException(String message) {
		super(message);
	}
}
