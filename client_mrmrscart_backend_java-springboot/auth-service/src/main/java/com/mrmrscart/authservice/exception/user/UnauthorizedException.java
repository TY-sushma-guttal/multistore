package com.mrmrscart.authservice.exception.user;

@SuppressWarnings("serial")
public class UnauthorizedException extends RuntimeException{

	public UnauthorizedException(String message) {
		super(message);
	}

	
}
