package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class NoBankDetailsFoundException extends RuntimeException {

	public NoBankDetailsFoundException(String message) {
		super(message);
	}

}
