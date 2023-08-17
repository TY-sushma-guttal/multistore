package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class InvalidBankDetailsException extends RuntimeException {
	
	public InvalidBankDetailsException(String message) {
		super(message);
	}


}
