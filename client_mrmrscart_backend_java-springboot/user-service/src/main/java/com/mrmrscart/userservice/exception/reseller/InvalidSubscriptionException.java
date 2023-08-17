package com.mrmrscart.userservice.exception.reseller;

@SuppressWarnings("serial")
public class InvalidSubscriptionException extends RuntimeException{
	public InvalidSubscriptionException(String message) {
		super(message);
	}
}
