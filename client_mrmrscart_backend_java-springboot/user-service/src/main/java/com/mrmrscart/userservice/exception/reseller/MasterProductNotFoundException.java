package com.mrmrscart.userservice.exception.reseller;

@SuppressWarnings("serial")
public class MasterProductNotFoundException extends RuntimeException{
	public MasterProductNotFoundException(String message) {
		super(message);
	}
}
