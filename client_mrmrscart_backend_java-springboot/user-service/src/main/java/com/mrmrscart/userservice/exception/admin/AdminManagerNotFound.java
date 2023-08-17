package com.mrmrscart.userservice.exception.admin;

@SuppressWarnings("serial")
public class AdminManagerNotFound extends RuntimeException{
	public AdminManagerNotFound(String message) {
		super(message);
	}
}
