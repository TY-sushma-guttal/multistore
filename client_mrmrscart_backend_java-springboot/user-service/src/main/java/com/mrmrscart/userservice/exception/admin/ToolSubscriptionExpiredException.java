package com.mrmrscart.userservice.exception.admin;

@SuppressWarnings("serial")
public class ToolSubscriptionExpiredException extends RuntimeException {
	public ToolSubscriptionExpiredException(String message) {
		super(message);
	}
}
